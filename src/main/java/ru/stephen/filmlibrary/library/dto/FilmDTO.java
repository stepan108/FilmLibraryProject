package ru.stephen.filmlibrary.library.dto;

import lombok.*;
import ru.stephen.filmlibrary.library.model.Genre;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO
        extends GenericDTO {
    private String filmTitle;
    private LocalDate premierYear;
    private String country;
    private Integer minLength;
    private String description;
    private Genre genre;
    private String onlineCopyPath;
    private List<Long> directorsIds;
    private Integer rating;
    private List<DirectorDTO> directorInfo;
}
