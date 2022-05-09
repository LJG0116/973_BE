package com.nst.fitnessu.dto;

import com.nst.fitnessu.domain.Message;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "로그인 response 정보", description = "엑세스 토큰과 메세지 리턴")
public class MessageDto {
    private Long send;
    private Long receive;
    private String roomId;
    private Message message;
}
