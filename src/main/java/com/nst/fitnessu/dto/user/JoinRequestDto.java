package com.nst.fitnessu.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "회원 가입 요청 정보", description = "이메일, 비밀번호, 비밀번호 확인, 이름 필요")
public class JoinRequestDto {

    private String email;

    private String password;

    private String confirmPassword;

    private String nickname;

}
