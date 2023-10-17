package ru.stephen.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stephen.filmlibrary.library.dto.FilmDTO;
import ru.stephen.filmlibrary.library.model.Film;
import ru.stephen.filmlibrary.library.model.GenericModel;
import ru.stephen.filmlibrary.library.repository.DirectorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FilmMapper
        extends GenericMapper<Film, FilmDTO> {
    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public FilmMapper(ModelMapper modelMapper,
                      DirectorRepository directorRepository,
                      DirectorMapper directorMapper) {
        super(Film.class, FilmDTO.class, modelMapper);
        this.directorRepository = directorRepository;
        this.directorMapper = directorMapper;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Film.class, FilmDTO.class)
                .addMappings(
                        mapper -> {
                            mapper.skip(FilmDTO::setDirectorsIds);
                            mapper.skip(FilmDTO::setDirectorInfo);
                        })
                .setPostConverter(toDTOConverter());
//                .addMappings(mapper -> mapper.skip(FilmDTO::setDirectorsIds)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(FilmDTO.class, Film.class)
                .addMappings(mapper -> mapper.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(FilmDTO source, Film destination) {
        if (!Objects.isNull(source.getDirectorsIds())) {
            destination.setDirectors(directorRepository.findAllById(source.getDirectorsIds()));
        } else {
            destination.setDirectors(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Film source, FilmDTO destination) {
        destination.setDirectorsIds(getIds(source));
//        destination.setDirectorInfo(directorMapper.toDTOs(source.getDirectors()));
    }

    @Override
    protected List<Long> getIds(Film entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getDirectors())
                ? null
                : entity.getDirectors().stream()
                    .map(GenericModel::getId)
                    .collect(Collectors.toList());
    }
}
