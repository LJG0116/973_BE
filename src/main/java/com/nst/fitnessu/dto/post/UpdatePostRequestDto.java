package com.nst.fitnessu.dto.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdatePostRequestDto {
    String title;
    String area;
    String category;
    String text;
    String author;
    String postId;
}
