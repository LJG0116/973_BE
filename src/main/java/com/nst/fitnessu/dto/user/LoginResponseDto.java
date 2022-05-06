package com.nst.fitnessu.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "로그인 response 정보", description = "엑세스 토큰, 이메일 닉네임")
public class LoginResponseDto {
    String accessToken;
    String userId;
    String email;
    String nickname;
    //String refreshToken;
}
