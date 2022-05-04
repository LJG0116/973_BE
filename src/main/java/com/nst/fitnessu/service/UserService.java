package com.nst.fitnessu.service;

import com.nst.fitnessu.config.JwtTokenProvider;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.LoginResponseDto;
import com.nst.fitnessu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;//

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
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public void validateDuplicateNickname(String nickname) {
        userRepository.findByEmail(nickname)
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }


    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findOne(String email) {
        return userRepository.findByEmail(email);
    }

}