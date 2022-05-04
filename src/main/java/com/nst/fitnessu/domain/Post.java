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

    @OneToMany(mappedBy = "post")
    private List<Area> areas;

    @OneToMany(mappedBy = "post")
    private List<Category> categories;

    private int viewCount;

    private LocalDateTime postDate;//

    //연관관계 메서드
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

}
