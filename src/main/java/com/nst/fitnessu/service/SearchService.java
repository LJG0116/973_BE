package com.nst.fitnessu.service;

import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.SearchPostRequestDto;
import com.nst.fitnessu.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

    public List<PostListResponseDto> searchPost(SearchPostRequestDto requestDto, Integer pageNum, Integer postsPerPage) {
        return postRepository.findAllByCondition(requestDto, PageRequest.of(pageNum - 1, postsPerPage));
    }
}
