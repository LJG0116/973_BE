package com.nst.fitnessu.service;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.myPage.MyPostRequestDto;
import com.nst.fitnessu.repository.PostRepository;
import com.nst.fitnessu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;//
    private final PostRepository postRepository;

    public List<PostListResponseDto> viewMyPost(MyPostRequestDto requestDto, Integer pageNum,Integer postsPerPage) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("해당 id의 회원을 찾을 수 없습니다"));

        Page<Post> page = postRepository.findByUser(user,
                // PageRequest의 page는 0부터 시작
                PageRequest.of(pageNum - 1, postsPerPage,
                        Sort.by(Sort.Direction.DESC, "postDate")
                ));
        return page.stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }
}
