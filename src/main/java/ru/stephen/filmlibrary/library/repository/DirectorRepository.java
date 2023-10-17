package ru.stephen.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stephen.filmlibrary.library.model.Director;
import ru.stephen.filmlibrary.library.model.Film;

@Repository
public interface DirectorRepository
        extends GenericRepository<Director> {
    Page<Director> findAllByDirectorFIOContainsIgnoreCaseAndIsDeletedFalse(String fio,
                                                                       Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select distinct d.*
                       from directors d
                                left join films_directors fd on d.id = fd.film_id
                                left join films f on f.id = fd.director_id
                       where d.director_fio ilike '%' || coalesce(:fio, '%')  || '%'
                         and coalesce(f.title, '%') ilike '%' ||  coalesce(:title, '%')  || '%'
                         and f.is_deleted = false
                    
                    """)
    Page<Director> searchDirectors(@Param(value = "title") String title,
                           @Param(value = "fio") String directorFio,
                           Pageable pageRequest);


    @Query(value = """
          select case when count(d) > 0 then false else true end
          from Director d join d.films f
                        join Order o on f.id = o.film.id
          where d.id = :directorId
          and o.returned = false
          """)
    boolean checkDirectorForDeletion(final Long directorId);
}
