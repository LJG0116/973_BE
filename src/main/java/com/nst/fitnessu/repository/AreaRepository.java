package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Area;
import com.nst.fitnessu.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area,Long> {

    Optional<Area> findByName(String name);
}
