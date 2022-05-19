package com.nst.fitnessu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@AllArgsConstructor
@Data
public class ChatRoomJoinDto {
    private BigInteger chatRoomId;
    private BigInteger userId;
    private String nickname;
}
