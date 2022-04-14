package com.nst.fitnessu.service;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.Member;
import com.nst.fitnessu.dto.LoginRequestDto;
import com.nst.fitnessu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;//
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    //회원가입
    @Transactional
    public Long join(Member member){
        //같은 이름이 있는 중복 x
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public JSONObject login(LoginRequestDto loginRequestDto){
        JSONObject jsonObject=new JSONObject();
        try{
            Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
            if(!passwordEncoder.matches(loginRequestDto.getPassword(),member.getPassword())){
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }
            //성공 시에
            jsonObject.put("status",200);
            jsonObject.put("token",jwtTokenProvider.createToken(member.getEmail(),member.getRoles()));
        }catch(IllegalArgumentException e){
            //존재하지 않는 이메일 일때
            if(e.getMessage().equals("가입되지 않은 E-MAIL 입니다.")){
                jsonObject.put("status",416);
                jsonObject.put("errorMessage","가입되지 않은 E-MAIL 입니다.");
                //비밀번호 틀렸을 때
            }else{
                jsonObject.put("status",417);
                jsonObject.put("errorMessage","잘못된 비밀번호입니다.");
            }
        }finally {
            return jsonObject;
        }
    }


    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
