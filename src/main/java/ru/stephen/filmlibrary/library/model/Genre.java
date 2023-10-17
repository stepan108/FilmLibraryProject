package ru.stephen.filmlibrary.library.model;

public enum Genre {
    SCIENCE_FICTION("Научная фантастика"),
    FANTASY("Фэнтези"),
    HORROR("Ужасы"),
    ACTON("Боевик"),
    COMEDY("Комедия"),
    DRAMA("Драма");

    private final String genreTextDisplay;

    Genre(String text) {
        this.genreTextDisplay = text;
    }

    public String getGenreTextDisplay() {
        return genreTextDisplay;
    }
}
