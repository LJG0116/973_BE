package com.nst.fitnessu.dto.post;

import com.nst.fitnessu.domain.Image;
import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    String nickname;
    String type;
    String profileImage;
    List<String> postImages;
    Long userId;
    LocalDateTime date;

    public ViewPostResponseDto(Post post, User user) {
        this.title = post.getTitle();
        this.area = post.getArea().split("#");
        this.category = post.getCategory().split("#");
        this.text = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.userId=user.getId();
        this.date = post.getPostDate();
        this.type = post.getType().toString();
        this.profileImage = user.getProfileImage();
        this.postImages=new ArrayList<>();
        for(Image image : post.getImages())
            postImages.add(image.getImageUrl());
    }
}
