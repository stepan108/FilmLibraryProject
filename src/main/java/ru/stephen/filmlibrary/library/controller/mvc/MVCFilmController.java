package ru.stephen.filmlibrary.library.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;
import ru.stephen.filmlibrary.library.dto.DirectorDTO;
import ru.stephen.filmlibrary.library.dto.FilmDTO;
import ru.stephen.filmlibrary.library.dto.FilmSearchDTO;
import ru.stephen.filmlibrary.library.exception.MyDeleteException;
import ru.stephen.filmlibrary.library.service.FilmService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/films")
public class MVCFilmController {
    private final FilmService filmService;

    public MVCFilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "filmTitle"));
        Page<FilmDTO> films = filmService.getAllFilms(pageRequest);
        model.addAttribute("films", films);
        model.addAttribute("exception", exception);
        return "films/viewAllFilms";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmService.getOne(id));
        return "films/viewFilm";
    }

    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmDTO newFilm,
                         @RequestParam(value = "onlineCopy", required = false) MultipartFile file) {
        if (file != null && file.getSize() > 0) {
            log.info(file.getName());
            filmService.create(newFilm, file);
        } else {
            filmService.create(newFilm);
        }
        return "redirect:/films";
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmService.getOne(id));
        return "films/updateFilm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("filmForm") FilmDTO filmDTO) {
        filmService.update(filmDTO);
        return "redirect:/films";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        filmService.deleteSoft(id);
        return "redirect:/films";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        filmService.restore(id);
        return "redirect:/films";
    }


    @PostMapping("/search")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("filmSearchForm") FilmSearchDTO filmSearchDTO,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("films", filmService.searchFilm(filmSearchDTO, pageRequest));
        return "films/viewAllFilms";
    }


    /**
     * Метод для поиска фильма по ФИО режиссера (редирект по кнопке "Посмотреть фильмы" на странице режиссера)
     *
     * @param page        - текущая страница
     * @param pageSize    - количество объектов на странице
     * @param directorDTO - ДТО режиссера
     * @param model       - модель
     * @return - форму со списком всех книг подходящих под критерии (по фио режиссера)
     */
    @PostMapping("/search/filmsByDirector")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("directorSearchForm") DirectorDTO directorDTO,
                              Model model) {
        FilmSearchDTO filmSearchDTO = new FilmSearchDTO();
        filmSearchDTO.setDirectorFio(directorDTO.getDirectorFIO());
        return searchFilms(page, pageSize, filmSearchDTO, model);
    }

    @ResponseBody
    @GetMapping(value = "/download", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> downloadFilm(@Param(value = "filmId") Long filmId) throws IOException {
        FilmDTO filmDTO = filmService.getOne(filmId);
        Path path = Paths.get(filmDTO.getOnlineCopyPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(createHeaders(path.getFileName().toString()))
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
   }

    private HttpHeaders createHeaders(final String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        headers.add("Cache-Control", "no-cache, no-store");
        headers.add("Expires", "0");
        return headers;
    }

    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class, NotFoundException.class})
    public RedirectView handleError(HttpServletRequest request,
                                    Exception exception,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос " + request.getRequestURL() + " вызвал ошибку: " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return  new RedirectView("/films", true);
    }
}
