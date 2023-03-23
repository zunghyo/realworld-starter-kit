package com.realworld.realworld.domain.user.service;

import com.realworld.realworld.domain.user.dto.UserRegisterRequestDto;
import com.realworld.realworld.domain.user.dto.UserRegisterResponseDto;
import com.realworld.realworld.domain.user.entity.User;
import com.realworld.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registerUser(final UserRegisterRequestDto userDto) {
        if(isEmailDuplicated(userDto.getEmail()))
            throw new IllegalArgumentException("Email 주소는 이미 사용중 입니다.");
        if(isUsernameDuplicated(userDto.getUsername()))
            throw new IllegalArgumentException("사용자 이름은 이미 사용중 입니다.");

        User entity = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncode(userDto.getPassword()))
                .username(userDto.getUsername())
                .build();

        return userRepository.save(entity).getId();
    }

    public UserRegisterResponseDto findUserById(final Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return new UserRegisterResponseDto(entity);
    }

    public String passwordEncode(String password){
        return passwordEncoder.encode(password);
    }

    private boolean isEmailDuplicated(final String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isUsernameDuplicated(final String username) {
        return userRepository.existsByUsername(username);
    }

}
