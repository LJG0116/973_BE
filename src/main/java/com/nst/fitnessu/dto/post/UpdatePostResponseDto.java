package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostResponseDto {

    String title;
    String[] area;
    String[] category;
    String text;
    String author;
    LocalDateTime date;

    public UpdatePostResponseDto(Post post) {
        this.title = post.getTitle();
        this.area = post.getArea().split("#");
        this.category = post.getCategory().split("#");
        this.text = post.getContent();
        this.author = post.getUser().getNickname();
        this.date = post.getPostDate();
    }
}
