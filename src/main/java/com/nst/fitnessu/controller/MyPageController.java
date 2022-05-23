package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.myPage.MyPostRequestDto;
import com.nst.fitnessu.dto.myPage.UpdateMyInfoRequestDto;
import com.nst.fitnessu.dto.myPage.ViewMyInfoResponseDto;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.ViewPostRequestDto;
import com.nst.fitnessu.dto.post.ViewPostResponseDto;
import com.nst.fitnessu.service.AwsS3Service;
import com.nst.fitnessu.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/myPage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/post")
    @ApiOperation(value = "내 게시글 목록")
    public ResponseEntity<ResultResponse> viewPostList(@RequestParam @ApiParam Integer page,
                                                            @RequestParam @ApiParam Long id) {
        MyPostRequestDto requestDto = new MyPostRequestDto(id);
        List<PostListResponseDto> pageList=myPageService.viewMyPost(requestDto,page,5);
        ResultResponse<List<PostListResponseDto>> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("나의 글목록 조회",pageList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "내 정보 조회")
    public ResponseEntity<ResultResponse> viewMyInfo(@PathVariable @ApiParam Long id) {
        //임시
        ViewMyInfoResponseDto responseDto = myPageService.viewMyInfo(id);
        ResultResponse<ViewMyInfoResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("내 정보 조회",responseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    @ApiOperation(value = "내 정보 수정")
    public ResponseEntity<ResultResponse> updateMyInfo(@RequestBody @ApiParam UpdateMyInfoRequestDto requestDto) {
        //임시
        ViewMyInfoResponseDto responseDto = myPageService.updateMyInfo(requestDto);
        ResultResponse<ViewMyInfoResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("내 정보 수정",responseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
