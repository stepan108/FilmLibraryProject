package ru.stephen.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stephen.filmlibrary.library.model.Film;

@Repository
public interface FilmRepository
        extends GenericRepository<Film> {
    @Query(nativeQuery = true,
            value = """
                    select distinct f.*
                       from films f
                                left join films_directors fd on f.id = fd.film_id
                                left join directors d on d.id = fd.director_id
                       where f.title ilike '%' || coalesce(:title, '%')  || '%'
                         and cast(f.genre as char) like coalesce(:genre, '%')
                         and coalesce(d.director_fio, '%') ilike '%' ||  coalesce(:fio, '%')  || '%'
                         and f.is_deleted = false
                    
                    """)
    Page<Film> searchFilms(@Param(value = "title") String title,
                           @Param(value = "genre") String genre,
                           @Param(value = "fio") String directorFio,
                           Pageable pageRequest);

    @Query("""
          select case when count(f) > 0 then false else true end
          from Film f join Order o on f.id = o.film.id
          where f.id = :id and o.returned = false
          """)
    boolean isFilmCanBeDeleted(final Long id);

    Page<Film> findAllByFilmTitle(String title,
                              Pageable pageable);
}
