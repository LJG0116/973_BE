package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewPostResponseDto {
    String title;
    String[] area;
    String[] category;
    String text;
    String nickname;
    String type;
    Long userId;
    LocalDateTime date;

    public ViewPostResponseDto(Post post) {
        this.title = post.getTitle();
        this.area = post.getArea().split("#");
        this.category = post.getCategory().split("#");
        this.text = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.userId=post.getUser().getId();
        this.date = post.getPostDate();
        this.type = post.getType().toString();
    }
}
