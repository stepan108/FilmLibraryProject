package ru.stephen.filmlibrary.library.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.stephen.filmlibrary.library.model.Genre;

@Getter
@Setter
@ToString
public class FilmSearchDTO {
    private String filmTitle;
    private String directorFio;
    private Genre genre;
}
