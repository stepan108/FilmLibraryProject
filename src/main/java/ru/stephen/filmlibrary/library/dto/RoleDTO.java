package ru.stephen.filmlibrary.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class RoleDTO
        extends GenericDTO {
    private Long id;
    private String title;
    private String description;
}
