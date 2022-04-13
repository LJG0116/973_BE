package com.nst.fitnessu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private Boolean enabled;

    private List<Role> roles;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();
}
