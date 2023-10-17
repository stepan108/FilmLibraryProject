package ru.stephen.filmlibrary.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "directors_sequence", allocationSize = 1)
public class Director extends GenericModel {

    @Column(name = "director_fio", nullable = false)
    private String directorFIO;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "biography", nullable = false)
    private String biography;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @JoinTable(name = "films_directors",
            joinColumns = @JoinColumn(name = "director_id"), foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"), inverseForeignKey = @ForeignKey(name = "FK_FILM_DIRECTORS"))
    List<Film> films;


}
