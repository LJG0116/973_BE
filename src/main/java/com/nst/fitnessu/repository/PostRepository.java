package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    Optional<Post> findByTitle(String title);

    Page<Post> findByType(Type type, Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);
}
