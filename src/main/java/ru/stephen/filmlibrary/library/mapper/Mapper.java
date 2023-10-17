package ru.stephen.filmlibrary.library.mapper;

import ru.stephen.filmlibrary.library.dto.GenericDTO;
import ru.stephen.filmlibrary.library.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {
    E toEntity(D dto);
    D toDTO(E entity);
    List<E> toEntities(List<D> dtos);
    List<D> toDTOs(List<E> entities);
}
