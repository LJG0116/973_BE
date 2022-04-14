package com.nst.fitnessu.controller;

import com.nst.fitnessu.dto.LoginRequestDto;
import com.nst.fitnessu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public JSONObject login(LoginRequestDto loginRequestDto){
        return memberService.login(loginRequestDto);
    }
}
