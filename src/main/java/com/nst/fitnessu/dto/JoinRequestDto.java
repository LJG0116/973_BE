package com.nst.fitnessu.dto;

import lombok.Data;

@Data
public class JoinRequestDto {

    private Long id;

    private String email;

    private String password;

    private String confirmPassword;

    private String name;

}
