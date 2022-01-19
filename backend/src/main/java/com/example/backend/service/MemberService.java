package com.example.backend.service;

import com.example.backend.entity.Member;
import com.example.backend.request.MemberRequest;

public interface MemberService {

    public void register(MemberRequest memberRequest) throws Exception;
    public MemberRequest login(MemberRequest memberRequest) throws Exception;
    public void pause (Member member, String email) throws Exception;

    // -- 회원정보 확인, 수정, 탈퇴, 아이디찾기, 비밀번호찾기(변경), 이메일 인증 --
    public boolean checkUserIdValidation(String UserId) throws Exception;
    public boolean checkDuplicateEmail(String Email) throws Exception;
}
