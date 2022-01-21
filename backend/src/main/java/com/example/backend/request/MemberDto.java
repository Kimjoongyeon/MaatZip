package com.example.backend.request;

import com.example.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String password;
    private String email;
    private String name;
    private String phone;

    public Member toEntity() {
        return Member.builder()
                .password(password)
                .email(email)
                .name(name)
                .phone(phone)
                .build();
    }

}
