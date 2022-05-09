package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByTitle(String title);

    Page<Post> findAllByType(Type type, Pageable pageable);
}
