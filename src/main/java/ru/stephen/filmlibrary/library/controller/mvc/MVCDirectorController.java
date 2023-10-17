package ru.stephen.filmlibrary.library.controller.mvc;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.stephen.filmlibrary.library.dto.*;
import ru.stephen.filmlibrary.library.exception.MyDeleteException;
import ru.stephen.filmlibrary.library.service.DirectorService;
import ru.stephen.filmlibrary.library.service.FilmService;

import static ru.stephen.filmlibrary.library.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@Hidden
@RequestMapping("/directors")
public class MVCDirectorController {
    private final DirectorService directorService;
    private final FilmService filmService;

    public MVCDirectorController(DirectorService directorService,
                                 FilmService filmService) {
        this.directorService = directorService;
        this.filmService = filmService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<DirectorDTO> directors;
        if(ADMIN.equalsIgnoreCase(userName)) {
            directors = directorService.listAll(pageRequest);
        } else {
            directors = directorService.listAllNotDeleted(pageRequest);
        }
        model.addAttribute("directors", directors);
        return "directors/viewAllDirectors";
    }
    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id,
                         Model model) {
        model.addAttribute("director", directorService.getOne(id));
        return "directors/viewDirector";
    }


    @PostMapping("/search")
    public String searchDirectors(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("directorSearchForm") DirectorSearchDTO directorSearchDTO,
                              Model model) {
        if (StringUtils.hasText(directorSearchDTO.getDirectorFio()) || StringUtils.hasLength(directorSearchDTO.getDirectorFio())) {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
            model.addAttribute("directors", directorService.searchDirector(directorSearchDTO, pageRequest));
            return "films/viewAllFilms";
        }
        else {
            return "redirect:/directors";
        }
    }

    @PostMapping("/search/directorsByFilm")
    public String searchDirectors(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("filmSearchForm") FilmDTO filmDTO,
                              Model model) {
        DirectorSearchDTO directorSearchDTO = new DirectorSearchDTO();
        directorSearchDTO.setFilmTitle(filmDTO.getFilmTitle());
        return searchDirectors(page, pageSize, directorSearchDTO, model);
    }

    @GetMapping("/add")
    public String create() {
        return "/directors/addDirector";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDTO newDirector) {
        log.info(newDirector.toString());
        directorService.create(newDirector);
        return "redirect:/directors";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("director", directorService.getOne(id));
        return "directors/updateDirector";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorDTO directorDTO) {
        directorService.update(directorDTO);
        return "redirect:/directors";
    }

    @GetMapping("/add-film/{directorId}")
    public String addFilm(@PathVariable Long directorId,
                          Model model) {
        model.addAttribute("films", filmService.listAll());
        model.addAttribute("directorId", directorId);
        model.addAttribute("director", directorService.getOne(directorId).getDirectorFIO());
        return "directors/addDirectorFilm";
    }

    @PostMapping("/add-film")
    public String addFilm(@ModelAttribute("directorFilmForm") AddFilmDTO addFilmDTO) {
        directorService.addFilm(addFilmDTO);
        return "redirect:/directors";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        directorService.deleteSoft(id);
        return "redirect:/directors";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        directorService.restore(id);
        return "redirect:/directors";
    }
}
