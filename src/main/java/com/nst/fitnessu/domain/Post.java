package com.nst.fitnessu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

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
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    public void setContent(Content content) {
        this.content=content;
        content.setPost(this);
    }
}
