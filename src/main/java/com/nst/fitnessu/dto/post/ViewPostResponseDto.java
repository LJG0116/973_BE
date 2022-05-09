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
    List<String> area;
    String category;
    String text;
    String author;
    LocalDateTime date;

    public ViewPostResponseDto(Post post, List<String> area) {
        this.title = post.getTitle();
        this.area = area;
        this.category = post.getCategory().getName();
        this.text = post.getContent();
        this.author = post.getAuthor();
        this.date = post.getPostDate();
    }
}
