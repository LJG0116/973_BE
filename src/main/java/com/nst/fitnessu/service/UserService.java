package com.nst.fitnessu.service;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.user.LoginResponseDto;
import com.nst.fitnessu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;//

    @Transactional
    public LoginResponseDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return new LoginResponseDto(jwtTokenProvider.createJwtAccessToken(user.getEmail(),user.getRoles())
        ,user.getId().toString(),user.getEmail(), user.getNickname());
    }
    //회원가입
    @Transactional
    public Long join(User user){
        //같은 이름이 있는 중복 x
        validateDuplicateMember(user);
        userRepository.save(user);
        return user.getId();
    }

    public void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });
    }

    public void validateDuplicateNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m->{
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });
    }


    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findOne(String email) {
        return userRepository.findByEmail(email);
    }

}
