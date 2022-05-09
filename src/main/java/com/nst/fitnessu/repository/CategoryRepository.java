package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Category;
import com.nst.fitnessu.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);
}
