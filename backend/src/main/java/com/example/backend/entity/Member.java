package com.example.backend.entity;

import com.example.backend.request.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private int pause = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_no")
    private List<MemberAuth> authList = new ArrayList<MemberAuth>();

    public Member(String password, String email, String name, String phone, int pause) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.pause = pause;
    }

    public void addAuth(MemberAuth auth) {
        authList.add(auth);
    }

    public void clearAuthList () {
        authList.clear();
    }

    // 회원정보 변경
    public void modifyMember(MemberDto memberDto) {
        this.password = memberDto.getPassword();
        this.email = memberDto.getEmail();
        this.phone = memberDto.getPhone();
    }

    // 비밀번호 재설정
    public void modifyPassword(MemberDto memberDto) {
        this.password = memberDto.getPassword();
    }
}
