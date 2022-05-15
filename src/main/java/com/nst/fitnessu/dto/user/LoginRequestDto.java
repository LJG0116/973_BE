package com.nst.fitnessu.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "로그인 요청 정보", description = "이메일 비밀번호 필요")
public class LoginRequestDto {
    private String email;
    private String password;
}
