package com.example.ormcoursework.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {

    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String role;

    public UserDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
