package com.nst.fitnessu.domain;

import com.nst.fitnessu.dto.post.UpdatePostRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String title;

    @Column(length = 1000)
    private String content;

    private String nickname;

    private int viewCount;

    private LocalDateTime postDate;//

    private String area;

    private String category;

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    //연관관계 메서드
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

    public void updatePost(UpdatePostRequestDto requestDto) {
        this.nickname =requestDto.getNickname();
        this.category= requestDto.getCategory();
        this.area= requestDto.getArea();
        this.postDate=LocalDateTime.now().withNano(0);
        this.title=requestDto.getTitle();
        this.content=requestDto.getText();
    }

    public void deletePost(){
        this.user.getPosts().remove(this);
        this.user = null;
    }
}
