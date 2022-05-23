package com.nst.fitnessu.dto.myPage;

import com.nst.fitnessu.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ViewMyInfoResponseDto {

    private String email;

    private String nickname;

    private String intro;

    private String profileImage;

    public ViewMyInfoResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.profileImage = user.getProfileImage();
    }
}
