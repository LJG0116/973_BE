package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.SearchPostRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListResponseDto> findAllByCondition(SearchPostRequestDto requestDto, Pageable pageable);
}
