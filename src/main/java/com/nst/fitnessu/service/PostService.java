package com.nst.fitnessu.service;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.*;
import com.nst.fitnessu.dto.post.*;
import com.nst.fitnessu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final CategoryRepository categoryRepository;
    private final AreaPostRepository areaPostRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ViewPostResponseDto createPost(CreatePostRequestDto requestDto,Type type) {

        User user = userRepository.findByNickname(requestDto.getAuthor())
                .orElseThrow(()->new IllegalArgumentException("해당 닉네임을 가진 유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .user(user)
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .postDate(LocalDateTime.now())
                .viewCount(0)
                .areaPosts(new ArrayList<>())
                .type(type)
                .content(requestDto.getText())
                .build();

        Category category=categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(()->new IllegalArgumentException("해당 카테고리는 없는 카테고리 입니다."));
        post.setCategory(category);

        postRepository.save(post);

        for(String request : requestDto.getArea()){
            Area area=areaRepository.findByName(request)
                    .orElseThrow(()->new IllegalArgumentException("해당 지역은 없는 지역입니다."));

            AreaPost areaPost=new AreaPost();
            areaPost.setArea(area);
            areaPost.setPost(post);
            areaPostRepository.save(areaPost);
        }
        return new ViewPostResponseDto(post, requestDto.getArea());
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

        List<AreaPost> areaPostList = post.getAreaPosts();
        List<String> area = new ArrayList<>();
        for(AreaPost areaPost : areaPostList){
            area.add(areaPost.getArea().getName());
        }

        ViewPostResponseDto responseDto = new ViewPostResponseDto(post,area);
        return responseDto;
    }


    @Transactional
    public UpdatePostResponseDto updatePost(UpdatePostRequestDto requestDto) {
        Post post = postRepository.findById(Long.parseLong(requestDto.getPostId()))
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));

        post.updatePost(requestDto);

        post.deleteCategory();
        Category category=categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(()->new IllegalArgumentException("해당 카테고리는 없는 카테고리 입니다."));
        post.setCategory(category);

        for(AreaPost areaPost : post.getAreaPosts()){
            areaPost.deleteAreaPost();
        }
        post.deleteArea();

        for(String request : requestDto.getArea()){
            Area area=areaRepository.findByName(request)
                    .orElseThrow(()->new IllegalArgumentException("해당 지역은 없는 지역입니다."));

            AreaPost areaPost=new AreaPost();
            areaPost.setArea(area);
            areaPost.setPost(post);
            areaPostRepository.save(areaPost);
        }

        UpdatePostResponseDto responseDto = new UpdatePostResponseDto(post,requestDto.getArea());
        return responseDto;
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 id의 post가 없습니다"));

        post.deleteCategory();;
        for(AreaPost areaPost : post.getAreaPosts()){
            areaPost.deleteAreaPost();
        }
        postRepository.deleteById(id);
    }
}
