package com.realworld.realworld.domain.user.dto;

import com.realworld.realworld.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequestDto {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String username;

    @Builder
    public UserRegisterRequestDto(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .build();
    }

}