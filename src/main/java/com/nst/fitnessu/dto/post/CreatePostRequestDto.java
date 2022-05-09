package com.nst.fitnessu.dto.post;

import lombok.Data;

@Data
public class CreatePostRequestDto {
    String title;
    String[] area;
    String category;
    String text;
    String userId;
    String author;
}
