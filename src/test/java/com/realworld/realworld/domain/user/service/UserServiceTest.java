package com.realworld.realworld.domain.user.service;

import com.realworld.realworld.domain.user.dto.UserRegisterRequestDto;
import com.realworld.realworld.domain.user.entity.User;
import com.realworld.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    private UserService userService;
    private UserRegisterRequestDto userA;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, new PasswordEncoderTest());
        userA = new UserRegisterRequestDto("aaa@gmail.com", "aaa", "사용자a");
    }

    @Test
    @DisplayName("유효한 회원 정보 입력 시 id 값 반환")
    void register_test01(){
        Long savedUserId = userService.registerUser(userA);
        assertThat(savedUserId).isNotNull();
    }

    @Test
    @DisplayName("중복된 이메일 입력 시 예외 발생")
    void register_test02(){
        userService.registerUser(userA);
        UserRegisterRequestDto userB = new UserRegisterRequestDto("aaa@gmail.com", "bbb", "사용자b");
        assertThatThrownBy(() -> userService.registerUser(userB))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email 주소는 이미 사용중 입니다.");
    }

    @Test
    @DisplayName("중복된 이름 입력 시 예외 발생")
    void register_test03(){
        userService.registerUser(userA);
        UserRegisterRequestDto userC = new UserRegisterRequestDto("bbb@gmail.com", "ccc", "사용자a");
        assertThatThrownBy(() -> userService.registerUser(userC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 이름은 이미 사용중 입니다.");
    }

    @Test
    @DisplayName("비밀번호는 암호화되어 저장")
    void register_test04(){
        Long registerUserId = userService.registerUser(userA);
        User registeredUser = userRepository.findById(registerUserId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        assertThat(registeredUser.getPassword()).isEqualTo("encodedassword_aaa");
    }

}