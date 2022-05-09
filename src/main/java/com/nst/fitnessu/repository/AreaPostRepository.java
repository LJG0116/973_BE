package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.AreaPost;
import com.nst.fitnessu.domain.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaPostRepository extends JpaRepository<AreaPost,Long> {

}
