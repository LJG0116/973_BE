package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListResponseDto {

    private Long postId;
    private String title;
    private String author;
    private LocalDateTime date;
    private String text;
    //private int viewCount;

    public PostListResponseDto(Post post) {
        this.postId= post.getId();
        this.title= post.getTitle();
        this.author= post.getAuthor();
        this.date=post.getPostDate();
        this.text=post.getContent();
      //  this.viewCount=post.getViewCount();
    }

    public PostListResponseDto(Long postId, String title, String author,String text, LocalDateTime date) {
        this.postId = postId;
        this.text=text;
        this.title = title;
        this.author = author;
        this.date = date;
    }
}
