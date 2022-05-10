package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.post.CreatePostRequestDto;
import com.nst.fitnessu.service.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create/category/{category}")
    @ApiOperation(value = "카테고리 추가")
    public ResponseEntity<ResultResponse> createCategory(@PathVariable @ApiParam String category) {
        adminService.createCategory(category);
        ResultResponse resultResponse=new ResultResponse(200,"카테고리 추가");
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/create/area/{area}")
    @ApiOperation(value = "지역 추가")
    public ResponseEntity<ResultResponse> createArea(@PathVariable @ApiParam String area) {
        adminService.createArea(area);
        ResultResponse resultResponse=new ResultResponse(200,"지역 추가");
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
