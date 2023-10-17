package ru.stephen.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.stephen.filmlibrary.library.constants.Errors;
import ru.stephen.filmlibrary.library.dto.FilmDTO;
import ru.stephen.filmlibrary.library.dto.FilmSearchDTO;
import ru.stephen.filmlibrary.library.exception.MyDeleteException;
import ru.stephen.filmlibrary.library.mapper.FilmMapper;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.model.Film;
import ru.stephen.filmlibrary.library.repository.DirectorRepository;
import ru.stephen.filmlibrary.library.repository.FilmRepository;
import ru.stephen.filmlibrary.library.utils.FileHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService
        extends GenericService<Film, FilmDTO> {
    private final DirectorRepository directorRepository;

    public FilmService(FilmRepository repository,
                       FilmMapper mapper,
                       DirectorRepository directorRepository) {
        super(repository, mapper);
        this.directorRepository = directorRepository;
    }

    public FilmDTO create(final FilmDTO newFilm,
                          MultipartFile file) {
        String filename = FileHelper.createFile(file);
        newFilm.setOnlineCopyPath(filename);
        newFilm.setCreatedWhen(LocalDateTime.now());
        newFilm.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return mapper.toDTO(repository.save(mapper.toEntity(newFilm)));
    }

    public Page<FilmDTO> getAllFilms(Pageable pageable) {
        Page<Film> filmsPaginated = repository.findAll(pageable);
        List<FilmDTO> result = mapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageable, filmsPaginated.getTotalElements());
    }

    public Page<FilmDTO> searchFilm(FilmSearchDTO filmSearchDTO,
                                    Pageable pageRequest) {
        String genre = filmSearchDTO.getGenre() != null
                ? String.valueOf(filmSearchDTO.getGenre().ordinal())
                : null;

        Page<Film> filmsPaginated = ((FilmRepository)repository).searchFilms(
                filmSearchDTO.getFilmTitle(),
                genre,
                filmSearchDTO.getDirectorFio(),
                pageRequest
        );

        List<FilmDTO> result = mapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, filmsPaginated.getTotalElements());
    }

    public Page<FilmDTO> searchFilm(final String filmTitle,
                                            Pageable pageable) {
        Page<Film> films = ((FilmRepository) repository).findAllByFilmTitle(filmTitle, pageable);
        List<FilmDTO> result = mapper.toDTOs(films.getContent());
        return new PageImpl<>(result, pageable, films.getTotalElements());

    }

    public FilmDTO addDirector(final Long filmId,
                               final Long directorId) {
        FilmDTO film = getOne(filmId);
        Director director = directorRepository.findById(directorId).orElseThrow(() -> new NotFoundException("Режиссер не найден"));
        film.getDirectorsIds().add(director.getId());
        update(film);
        return film;
    }

    @Override
    public void deleteSoft(final Long id) throws MyDeleteException {
        Film film = repository.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        boolean filmCanBeDeleted = ((FilmRepository)repository).isFilmCanBeDeleted(id);
        if (filmCanBeDeleted) {
            markAsDeleted(film);
            repository.save(film);
        }
        else {
            throw new MyDeleteException(Errors.Film.FILM_DELETE_ERROR);
        }
    }
}
