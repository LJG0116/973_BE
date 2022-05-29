package com.nst.fitnessu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChatRoomJoinResponseDto {
    private Long senderId;
    private String senderNickname;
    private Long chatRoomId;
    private Long receiverId;
    private String receiverNickname;
    private String profileImage;
}
