package com.nst.fitnessu.dto.post;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequestDto {
    String title;
    String area;
    String category;
    String text;
    String author;
}
