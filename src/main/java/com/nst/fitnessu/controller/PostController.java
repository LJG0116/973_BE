package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.post.CreatePostRequestDto;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.ViewPostRequestDto;
import com.nst.fitnessu.dto.post.ViewPostResponseDto;
import com.nst.fitnessu.dto.user.LoginResponseDto;
import com.nst.fitnessu.service.PostService;
import com.nst.fitnessu.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/create/coach")
    @ApiOperation(value = "코치 글쓰기")
    public ResponseEntity<ResultResponse> createCoachPost(@RequestBody @ApiParam CreatePostRequestDto requestDto) {
        Long postId=postService.createPost(requestDto,Type.coach);
        ResultResponse<String> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("글쓰기 성공",postId.toString());
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/create/player")
    @ApiOperation(value = "플레이어 글쓰기")
    public ResponseEntity<ResultResponse> createPlayerPost(@RequestBody @ApiParam CreatePostRequestDto requestDto) {
        Long postId=postService.createPost(requestDto,Type.player);
        ResultResponse<String> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("글쓰기 성공",postId.toString());
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/view/list/{page}")
    @ApiOperation(value = "게시글 목록")
    public ResponseEntity<ResultResponse> viewPostList(@PathVariable @ApiParam Integer page) {
        List<PostListResponseDto> pageList=postService.findAllByOrderByIdDesc(page,10);
        ResultResponse<List<PostListResponseDto>> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("글목록 조회",pageList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    @ApiOperation(value = "게시글 조회")
    public ResponseEntity<ResultResponse> viewPostList(@PathVariable @ApiParam String id) {
        //임시
        ViewPostRequestDto requestDto = new ViewPostRequestDto(id);
        ViewPostResponseDto responseDto=postService.findPost(requestDto);
        ResultResponse<ViewPostResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("게시글 조회",responseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
//    @GetMapping("/view")
//    @ApiOperation(value="게시물 조회")

}
