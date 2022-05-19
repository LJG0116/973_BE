package com.nst.fitnessu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoomEnterRequestDto {
    private Long userId;
    //private Long receiverId;
    private Long postId;
}
