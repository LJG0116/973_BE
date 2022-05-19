package com.nst.fitnessu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatRoomMessageDto {
    private Long userId;
    private String nickname;
    private Long roomId;
    private LocalDateTime messageTime;
    private String content;
}
