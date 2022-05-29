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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/myPage")
public class MyPageController {

    private final MyPageService myPageService;
    private final AwsS3Service awsS3Service;

    @GetMapping("/post")
    @ApiOperation(value = "내 게시글 목록")
    public ResponseEntity<ResultResponse> viewPostList(@RequestParam @ApiParam Integer page,
                                                            @RequestParam @ApiParam Long userId) {
        MyPostRequestDto requestDto = new MyPostRequestDto(userId);
        List<PostListResponseDto> pageList=myPageService.viewMyPost(requestDto,page,5);
        ResultResponse<List<PostListResponseDto>> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("나의 글목록 조회",pageList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/info/{userId}")
    @ApiOperation(value = "내 정보 조회")
    public ResponseEntity<ResultResponse> viewMyInfo(@PathVariable @ApiParam Long userId) {
        //임시
        ViewMyInfoResponseDto responseDto = myPageService.viewMyInfo(userId);
        ResultResponse<ViewMyInfoResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("내 정보 조회",responseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    @ApiOperation(value = "내 정보 수정")
    public ResponseEntity<ResultResponse> updateMyInfo(@RequestParam @ApiParam Long id,
                                                       @RequestParam @ApiParam String email,
                                                       @RequestParam(required = false) @ApiParam String intro,
                                                       @RequestParam @ApiParam String nickname,
                                                       @RequestPart(required = false) @ApiParam MultipartFile profileImage) {
        String imageUrl=awsS3Service.uploadImage(id,profileImage);
        UpdateMyInfoRequestDto requestDto = new UpdateMyInfoRequestDto(id, email, nickname, intro, imageUrl);
        ViewMyInfoResponseDto responseDto = myPageService.updateMyInfo(requestDto);
        ResultResponse<ViewMyInfoResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("내 정보 수정",responseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
