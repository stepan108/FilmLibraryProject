package ru.stephen.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stephen.filmlibrary.library.dto.GenericDTO;
import ru.stephen.filmlibrary.library.exception.MyDeleteException;
import ru.stephen.filmlibrary.library.model.GenericModel;
import ru.stephen.filmlibrary.library.service.GenericService;

import java.util.List;

@RestController
@Slf4j
public abstract class GenericController<E extends GenericModel, D extends GenericDTO> {
    protected GenericService<E, D> service;

    public GenericController(GenericService<E, D> genericService) {
        this.service = genericService;
    }

    @Operation(description = "Получить запись по ID", method = "getOneById")
    @RequestMapping(value = "/getOneById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> getOneById(@RequestParam("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getOne(id));
    }


    @Operation(description = "Получить все записи", method = "getAll")
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.listAll());
    }

    @Operation(description = "Создать запись", method = "add")
    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> create(@RequestBody D newEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(newEntity));
    }

    @Operation(description = "Обновить запись", method = "update")
    @RequestMapping(value = "/update",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> update(@RequestBody D updatedEntity,
                                    @RequestParam(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(updatedEntity));
    }

    //localhost:8080/authors/delete?id=1 - @RequestParam
    //localhost:8080/authors/delete/1 - @PathVariable
    @Operation(description = "Обновить запись", method = "update")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Long id) throws MyDeleteException {
        service.deleteSoft(id);
    }

    @Operation(description = "Удалить запись по ID", method = "delete")
    @RequestMapping(value = "/delete/hard/{id}", method = RequestMethod.DELETE)
    public void deleteHard(@PathVariable(value = "id") Long id) throws MyDeleteException {
        service.delete(id);
    }
}
