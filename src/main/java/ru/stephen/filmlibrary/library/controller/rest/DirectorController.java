package ru.stephen.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stephen.filmlibrary.library.dto.AddFilmDTO;
import ru.stephen.filmlibrary.library.dto.DirectorDTO;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.service.DirectorService;

@Hidden
@RestController
@RequestMapping("/api/rest/directors")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Режиссеры", description = "Контроллер для работы с режиссерами фильмов из фильмотеки")
public class DirectorController
        extends GenericController<Director, DirectorDTO> {
    public DirectorController(DirectorService directorService) {
        super(directorService);
    }

    @Operation(description = "Добавить фильм к режиссеру")
    @RequestMapping(value = "/addFilm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> addFilm(@RequestParam(value = "filmId") Long filmId,
                                               @RequestParam(value = "directorId") Long directorId) {
        AddFilmDTO addFilmDTO = new AddFilmDTO();
        addFilmDTO.setDirectorId(directorId);
        addFilmDTO.setFilmId(filmId);
        return ResponseEntity.status(HttpStatus.OK).body(((DirectorService) service).addFilm(addFilmDTO));
    }
}
