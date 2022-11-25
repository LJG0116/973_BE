package com.nst.fitnessu.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nst.fitnessu.dto.myPage.UpdateMyInfoRequestDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String intro;

    private String profileImage;

    private String roles;

    private Boolean enabled;

    //private String refreshToken;
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<ChatRoomJoin> chatRoomJoins;


    //@JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();


//    //refreshToken 갱신
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken=refreshToken;
//    }

    public void setPassword(String password) {
        this.password=password;
    }
    public void updateUser(UpdateMyInfoRequestDto requestDto){
        this.email= requestDto.getEmail();
        this.nickname= requestDto.getNickname();
        this.intro= requestDto.getIntro();
        this.profileImage= requestDto.getProfileImage();
    }
    //-----------------------인증관련--------------------------------

    public List<String> getRoleList() {
        if (this.roles.length() > 0)
            return Arrays.asList(this.roles.split(","));
        return new ArrayList<>();
    }


}
