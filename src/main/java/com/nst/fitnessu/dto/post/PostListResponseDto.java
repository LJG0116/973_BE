package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListResponseDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime date;
    //private int viewCount;

    public PostListResponseDto(Post post) {
        this.id= post.getId();
        this.title= post.getTitle();
        this.author= post.getAuthor();
        this.date=post.getPostDate();
      //  this.viewCount=post.getViewCount();
    }
}
