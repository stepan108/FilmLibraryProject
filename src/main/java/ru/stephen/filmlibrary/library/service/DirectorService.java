package ru.stephen.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.stephen.filmlibrary.library.constants.Errors;
import ru.stephen.filmlibrary.library.dto.*;
import ru.stephen.filmlibrary.library.exception.MyDeleteException;
import ru.stephen.filmlibrary.library.mapper.DirectorMapper;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.model.Film;
import ru.stephen.filmlibrary.library.repository.DirectorRepository;
import ru.stephen.filmlibrary.library.repository.FilmRepository;

import java.util.List;

@Service
public class DirectorService
        extends GenericService<Director, DirectorDTO> {
    public DirectorService(DirectorRepository directorRepository,
                           DirectorMapper directorMapper) {
        super(directorRepository, directorMapper);
    }

    public Page<DirectorDTO> searchDirector(DirectorSearchDTO directorSearchDTO,
                                            Pageable pageRequest) {
        Page<Director> directorsPaginated = ((DirectorRepository) repository).searchDirectors(
                directorSearchDTO.getFilmTitle(),
                directorSearchDTO.getDirectorFio(),
                pageRequest
        );

        List<DirectorDTO> result = mapper.toDTOs(directorsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, directorsPaginated.getTotalElements());
    }

    public DirectorDTO addFilm(final AddFilmDTO addFilmDTO) {
        DirectorDTO director = getOne(addFilmDTO.getDirectorId());
        director.getFilmsIds().add(addFilmDTO.getFilmId());
        update(director);
        return director;
    }

    @Override
    public void deleteSoft(Long objectId) throws MyDeleteException {
        Director director = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Режиссера с заданным id=" + objectId + " не существует."));
        boolean directorCanBeDeleted = ((DirectorRepository)repository).checkDirectorForDeletion(objectId);
        if (directorCanBeDeleted) {
            markAsDeleted(director);
            List<Film> films = director.getFilms();
            if (films != null && films.size() > 0) {
                films.forEach(this::markAsDeleted);
            }
            repository.save(director);
        }
        else {
            throw new MyDeleteException(Errors.Directors.DIRECTOR_DELETE_ERROR);
        }
    }

    public void restore(Long objectId) {
        Director director = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Режиссера с заданным id=" + objectId + " не существует."));
        unMarkAsDeleted(director);
        List<Film> films = director.getFilms();
        if (films != null && films.size() > 0) {
            films.forEach(this::unMarkAsDeleted);
        }
        repository.save(director);
    }
}
