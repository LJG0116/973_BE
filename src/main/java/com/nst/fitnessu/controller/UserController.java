package com.nst.fitnessu.controller;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.JoinRequestDto;
import com.nst.fitnessu.dto.LoginRequeustDto;
import com.nst.fitnessu.dto.LoginResponseDto;
import com.nst.fitnessu.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<String> join(@RequestBody @ApiParam(value="회원 정보를 가진 객체",required = true)
                                                   JoinRequestDto requestDto) {

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        //중복 회원 검증
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });

        userRepository.save(user);

        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 기능")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @ApiParam(value="이메일 비밀번호가 필요함", required = true)
                                                              LoginRequeustDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken(jwtTokenProvider.createToken(user.getEmail(), user.getRoles()))
                .message("로그인 성공")
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
