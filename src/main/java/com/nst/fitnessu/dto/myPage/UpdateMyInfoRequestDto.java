package com.nst.fitnessu.dto.myPage;

import lombok.Data;

@Data
public class UpdateMyInfoRequestDto {
    private Long id;

    private String email;

    private String nickname;

    private String intro;

    private String profileImage;

}
