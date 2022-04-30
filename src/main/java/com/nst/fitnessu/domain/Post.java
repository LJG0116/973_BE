package com.nst.fitnessu.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Embedded
    private Type type;

    private String title;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "content_id")
    private Content content;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    @OneToMany(mappedBy = "post")
    private List<Area> areas;

    private int viewCount;

    private LocalDateTime postDate;//


    //연관관계 메서드
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

}
