package com.nst.fitnessu.dto.myPage;

import lombok.Data;

@Data
public class UpdateMyInfoRequestDto {
    private Long id;

    private String email;

    private String nickname;

    private String intro;

    private String profileImage;

    public UpdateMyInfoRequestDto(Long id, String email, String nickname, String intro, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.intro = intro;
        this.profileImage = profileImage;
    }
}
