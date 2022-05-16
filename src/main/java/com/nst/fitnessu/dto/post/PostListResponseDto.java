package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListResponseDto {

    private Long postId;
    private String title;
    private String nickname;
    private String[] category;
    private String[] area;
    private LocalDateTime date;
    private String text;
    private String type;
    //private int viewCount;

    public PostListResponseDto(Post post) {
        this.postId= post.getId();
        this.title= post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.date=post.getPostDate();
        this.text=post.getContent();
        this.type=post.getType().toString();
      //  this.viewCount=post.getViewCount();
    }

    public PostListResponseDto(Long postId, String title, String nickname,
                               String area, String category, String text, Type type, LocalDateTime date) {
        this.postId = postId;
        this.text=text;
        this.area = area.split("#");
        this.category=category.split("#");
        this.title = title;
        this.nickname = nickname;
        this.type=type.toString();
        this.date = date;
    }
}
