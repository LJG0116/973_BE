package com.nst.fitnessu.dto.chat;

import com.nst.fitnessu.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId;
    private Long senderId;
    private String senderNickname;
    //private String nickname;
    private List<ChatRoomMessageDto> messages;

}
