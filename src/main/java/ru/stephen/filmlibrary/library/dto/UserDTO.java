package ru.stephen.filmlibrary.library.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO
        extends GenericDTO {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String email;
    private RoleDTO role;
    private String changePasswordToken;
    private List<Long> userOrders;
    private boolean isDeleted;
}
