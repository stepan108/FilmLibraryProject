package ru.stephen.filmlibrary.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "films_sequence", allocationSize = 1)
public class Film extends GenericModel {

    @Column(name = "title", nullable = false)
    private String filmTitle;

    @Column(name = "premier_year", nullable = false)
    private LocalDate premierYear;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "description")
    private String description;

    @Column(name = "genre", nullable = false)
    @Enumerated
    private Genre genre;

    @Column(name = "online_copy_path")
    private String onlineCopyPath;

    private Integer rating;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_directors",
            joinColumns = @JoinColumn(name = "film_id"), foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "director_id"), inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    List<Director> directors;

//    @OneToMany(mappedBy = "film")
//    private List<Order> orders;
}
