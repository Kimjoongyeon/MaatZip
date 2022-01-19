package com.example.backend.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRequest {
    private String password;
    private String email;
    private String name;
    private String phone;
    private int pause;

    private String auth;

    public MemberRequest(String email, String password, String auth) {
        this.email = email;
        this.password = password;
        this.auth = auth;
    }
}
