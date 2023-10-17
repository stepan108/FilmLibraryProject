package ru.stephen.filmlibrary.library.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDTO
        extends GenericDTO {
    private Long userId;
    private Long filmId;
    private LocalDateTime rentDate;
    private Integer rentPeriod;
    private LocalDateTime returnDate;
    private Boolean purchased;
    private Boolean returned;
    private FilmDTO filmDTO;
}
