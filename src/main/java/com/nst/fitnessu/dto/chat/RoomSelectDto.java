package com.nst.fitnessu.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomSelectDto {
    private Long userId;
    private Long roomId;
}
