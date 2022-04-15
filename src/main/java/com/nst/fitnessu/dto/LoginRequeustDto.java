package com.nst.fitnessu.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "로그인 요청 정보", description = "이메일 비밀번호 필요")
public class LoginRequeustDto {
    private String email;
    private String password;
}
