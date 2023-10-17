package ru.stephen.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.stephen.filmlibrary.library.dto.FilmDTO;
import ru.stephen.filmlibrary.library.model.Film;
import ru.stephen.filmlibrary.library.service.FilmService;

@RestController
@RequestMapping("/api/rest/films")
@Tag(name = "Книги", description = "Контроллер для работы с фильмами из фильмотеки")
public class FilmController extends GenericController<Film, FilmDTO> {
    protected FilmController(FilmService filmService) {
        super(filmService);
    }

    public ResponseEntity<FilmDTO> addDirector(@RequestParam(value = "filmId") Long filmId,
                                           @RequestParam(value = "directorId") Long directorId) {
        return ResponseEntity.status(HttpStatus.OK).body(((FilmService) service).addDirector(filmId, directorId));
    }
}
