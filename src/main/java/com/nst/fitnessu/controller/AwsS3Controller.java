package com.nst.fitnessu.controller;

import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.service.ImageService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/s3")
public class AwsS3Controller {
    private final ImageService imageService;

    @PutMapping
    public ResponseEntity<ResultResponse> uploadImage(@RequestParam(required = false) @ApiParam Long id,
                                                      @RequestPart @ApiParam MultipartFile multipartFile) {

        String response= imageService.uploadUserImage(id,multipartFile);
        ResultResponse<String> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("파일 업로드",response);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

}
