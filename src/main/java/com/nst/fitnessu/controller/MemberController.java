package com.nst.fitnessu.controller;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.Member;
import com.nst.fitnessu.dto.JoinRequestDto;
import com.nst.fitnessu.dto.LoginRequeustDto;
import com.nst.fitnessu.dto.LoginResponseDto;
import com.nst.fitnessu.repository.MemberRepository;
import com.nst.fitnessu.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<String> join(@RequestBody @ApiParam(value="회원 정보를 가진 객체",required = true)
                                                   JoinRequestDto requestDto) {

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        //중복 회원 검증
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });

        memberRepository.save(member);

        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 기능")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @ApiParam(value="이메일 비밀번호가 필요함", required = true)
                                                              LoginRequeustDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()))
                .message("로그인 성공")
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
