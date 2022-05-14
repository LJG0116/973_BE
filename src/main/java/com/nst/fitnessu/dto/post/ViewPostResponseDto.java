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
public class ViewPostResponseDto {
    String title;
    String[] area;
    String[] category;
    String text;
    String author;
    String type;
    LocalDateTime date;

    public ViewPostResponseDto(Post post) {
        this.title = post.getTitle();
        this.area = post.getArea().split("#");
        this.category = post.getCategory().split("#");
        this.text = post.getContent();
        this.author = post.getAuthor();
        this.date = post.getPostDate();
        this.type = post.getType().toString();
    }
}
