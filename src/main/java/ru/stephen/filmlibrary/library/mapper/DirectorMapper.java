package ru.stephen.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stephen.filmlibrary.library.dto.DirectorDTO;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.model.GenericModel;
import ru.stephen.filmlibrary.library.repository.FilmRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DirectorMapper extends
        GenericMapper<Director, DirectorDTO>{
    private final FilmRepository filmRepository;

    public DirectorMapper(ModelMapper modelMapper,
                          FilmRepository filmRepository) {
        super(Director.class, DirectorDTO.class, modelMapper);
        this.filmRepository = filmRepository;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        modelMapper.createTypeMap(Director.class, DirectorDTO.class)
                .addMappings(mapping -> mapping.skip(DirectorDTO::setFilmsIds)).setPostConverter(toDTOConverter());


        modelMapper.createTypeMap(DirectorDTO.class, Director.class)
                .addMappings(mapping -> mapping.skip(Director::setFilms)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(DirectorDTO source, Director destination) {
        if(!Objects.isNull(source.getFilmsIds())) {
            destination.setFilms(filmRepository.findAllById(source.getFilmsIds()));
        } else {
            destination.setFilms(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Director source, DirectorDTO destination) {
        destination.setFilmsIds(getIds(source));
    }

    @Override
    protected List<Long> getIds(Director source) {
        return Objects.isNull(source) || Objects.isNull(source.getFilms())
                ? null
                : source.getFilms().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}
