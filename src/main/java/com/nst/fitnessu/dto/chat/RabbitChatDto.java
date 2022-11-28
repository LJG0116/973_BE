package com.nst.fitnessu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class RabbitChatDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private Long roomId;
    private String messageTime;
    private String content;
}
