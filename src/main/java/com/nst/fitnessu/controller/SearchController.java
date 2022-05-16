package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.SearchPostRequestDto;
import com.nst.fitnessu.service.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/post/{page}")
    @ApiOperation(value = "게시글 검색")
    public ResponseEntity<ResultResponse> searchPostList(@RequestParam(required = false) @ApiParam String area,
                                                         @RequestParam(required = false) @ApiParam String category,
                                                         @RequestParam(required = false) @ApiParam String keyword,
                                                         @RequestParam(required = false) @ApiParam String type,
                                                         @PathVariable @ApiParam int page) {
        SearchPostRequestDto requestDto = new SearchPostRequestDto(area, category, keyword,type);
        List<PostListResponseDto> pageList=searchService.searchPost(requestDto,page,10);
        ResultResponse<List<PostListResponseDto>> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("게시글 검색",pageList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
