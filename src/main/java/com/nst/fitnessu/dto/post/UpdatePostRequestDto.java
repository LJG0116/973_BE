package com.nst.fitnessu.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UpdatePostRequestDto {
    String title;
    String area;
    String category;
    String text;
    String nickname;
    Long userId;
    Long postId;
}
