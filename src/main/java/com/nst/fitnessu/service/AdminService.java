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
        Category category=Category.builder()
                .name(name)
                .posts(new ArrayList<>())
                .build();
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public Long createArea(String name) {
        Area area = Area.builder()
                .name(name)
                .areaPosts(new ArrayList<>())
                .build();
        areaRepository.save(area);
        return area.getId();
    }

}
