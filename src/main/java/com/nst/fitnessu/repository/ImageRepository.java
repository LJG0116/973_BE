package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
