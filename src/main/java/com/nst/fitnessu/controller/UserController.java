package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.*;
import com.nst.fitnessu.dto.user.JoinRequestDto;
import com.nst.fitnessu.dto.user.LoginRequestDto;
import com.nst.fitnessu.dto.user.LoginResponseDto;
import com.nst.fitnessu.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*",maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<String> join(@RequestBody @ApiParam(value="회원 정보를 가진 객체",required = true)
                                               JoinRequestDto requestDto) {
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .profileImage("https://974s3.s3.ap-northeast-2.amazonaws.com/user.png")
                .build();

        userService.join(user);

        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "이메일 중복 체크")
    public ResponseEntity<ResultResponse> checkEmail(@PathVariable @ApiParam(value="이메일", required = true) String email) {
        userService.validateDuplicateEmail(email);
        ResultResponse resultResponse = new ResultResponse(200, "이메일 중복 없음");
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/nickname/{nickname}")
    @ApiOperation(value = "닉네임 중복 체크")
    public ResponseEntity<ResultResponse> checkNickname(@PathVariable @ApiParam(value="닉네임",required = true) String nickname) {
        userService.validateDuplicateNickname(nickname);
        ResultResponse resultResponse = new ResultResponse(200, "닉네임 중복 없음");
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 기능")
    public ResponseEntity<ResultResponse> login(@RequestBody @ApiParam(value="이메일 비밀번호가 필요함", required = true)
                                                        LoginRequestDto requestDto) {
        LoginResponseDto loginResponseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());
        ResultResponse<LoginResponseDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("로그인 성공",loginResponseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
