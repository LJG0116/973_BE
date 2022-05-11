package com.nst.fitnessu.service;

import com.nst.fitnessu.domain.Area;
import com.nst.fitnessu.domain.Category;
import com.nst.fitnessu.repository.AreaRepository;
import com.nst.fitnessu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final CategoryRepository categoryRepository;
    private final AreaRepository areaRepository;

    @Transactional
    public Long createCategory(String name) {
        categoryRepository.findByName(name)
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
                });

        Category category=Category.builder()
                .name(name)
                .posts(new ArrayList<>())
                .build();
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public Long createArea(String name) {

        areaRepository.findByName(name)
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는지역 입니다.");
                });

        Area area = Area.builder()
                .name(name)
                .areaPosts(new ArrayList<>())
                .build();
        areaRepository.save(area);
        return area.getId();
    }

}
