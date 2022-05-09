package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;

import java.time.LocalDateTime;

public class PostListResponseDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime postDate;
    //private int viewCount;

    public PostListResponseDto(Post post) {
        this.id= post.getId();
        this.title= post.getTitle();
        this.author= post.getAuthor();
        this.postDate=post.getPostDate();
      //  this.viewCount=post.getViewCount();
    }
}
