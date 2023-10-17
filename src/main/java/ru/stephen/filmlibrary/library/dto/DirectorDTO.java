package ru.stephen.filmlibrary.library.dto;

import lombok.*;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DirectorDTO
        extends GenericDTO {
    private String directorFIO;
    private LocalDate birthDate;
    private String biography;
    private List<Long> filmsIds;

    public DirectorDTO(Director director) {
        this.birthDate = director.getBirthDate();
        this.createdBy = director.getCreatedBy();
        this.directorFIO = director.getDirectorFIO();
        this.biography = director.getBiography();
        this.createdWhen = director.getCreatedWhen();
        this.id = director.getId();
        List<Film> films = director.getFilms();
        List<Long> filmIds = new ArrayList<>();
        films.forEach(f -> filmIds.add(f.getId()));
        this.filmsIds = filmIds;
        this.isDeleted = false;
    }
}
