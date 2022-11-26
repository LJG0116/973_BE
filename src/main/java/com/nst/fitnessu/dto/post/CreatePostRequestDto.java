package com.nst.fitnessu.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePostRequestDto {
    String title;
    String area;
    String category;
    String text;
    String nickname;
    Long userId;
}
