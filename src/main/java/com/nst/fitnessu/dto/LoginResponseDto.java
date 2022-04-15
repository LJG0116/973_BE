package com.nst.fitnessu.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "로그인 response 정보", description = "엑세스 토큰과 메세지 리턴")
public class LoginResponseDto {
    String accessToken;
    String message;
}
