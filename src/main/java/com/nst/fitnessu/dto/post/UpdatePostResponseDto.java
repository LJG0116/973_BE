package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostResponseDto {

    String title;
    String[] area;
    String[] category;
    String text;
    String nickname;
    Long userId;
    LocalDateTime date;

    public UpdatePostResponseDto(Post post, User user) {
        this.title = post.getTitle();
        this.area = post.getArea().split("#");
        this.category = post.getCategory().split("#");
        this.text = post.getContent();
        this.nickname = user.getNickname();
        this.userId=user.getId();
        this.date = post.getPostDate();
    }
}
