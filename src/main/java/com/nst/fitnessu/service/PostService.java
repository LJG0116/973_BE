package com.nst.fitnessu.service;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.*;
import com.nst.fitnessu.dto.chat.RoomEnterRequestDto;
import com.nst.fitnessu.dto.post.*;
import com.nst.fitnessu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreatePostResponseDto createPost(CreatePostRequestDto requestDto,Type type) {

        User user = userRepository.findByNickname(requestDto.getAuthor())
                .orElseThrow(()->new IllegalArgumentException("해당 닉네임을 가진 유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .area(requestDto.getArea())
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .postDate(LocalDateTime.now().withNano(0))
                .viewCount(0)
                .type(type)
                .content(requestDto.getText())
                .build();

        post.setUser(user);
        postRepository.save(post);

        return new CreatePostResponseDto(post.getId());
    }

    public List<PostListResponseDto> viewList(Type type,Integer pageNum, Integer postsPerPage) {
        Page<Post> page = postRepository.findByType(type,
                // PageRequest의 page는 0부터 시작
                PageRequest.of(pageNum - 1, postsPerPage,
                        Sort.by(Sort.Direction.DESC, "postDate")
                ));
        return page.stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    public ViewPostResponseDto findPost(ViewPostRequestDto requestDto) {
        Post post = postRepository.findById((requestDto.getPostId()))
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));
        ViewPostResponseDto responseDto = new ViewPostResponseDto(post);
        return responseDto;
    }

    public User findPostInChat(RoomEnterRequestDto requestDto){
        Post post = postRepository.findById((requestDto.getPostId()))
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));
        return post.getUser();
    }

    @Transactional
    public UpdatePostResponseDto updatePost(UpdatePostRequestDto requestDto) {
        Post post = postRepository.findById(Long.parseLong(requestDto.getPostId()))
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));

        post.updatePost(requestDto);

        UpdatePostResponseDto responseDto = new UpdatePostResponseDto(post);
        return responseDto;
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));

        post.deletePost();
        postRepository.deleteById(id);
    }
}
