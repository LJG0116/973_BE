package com.nst.fitnessu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Long id;

    String name;

    @OneToMany(mappedBy = "area",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaPost> areaPosts;
}
