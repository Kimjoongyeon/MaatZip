package com.example.backend.service;

import com.example.backend.entity.Member;
import com.example.backend.entity.MemberAuth;
import com.example.backend.repository.MemberAuthRepository;
import com.example.backend.repository.MemberRepository;
import com.example.backend.request.MemberRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Lazy
@Slf4j
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberAuthRepository authRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void register(MemberRequest memberRequest) throws Exception {
        String encodedPassword = encoder.encode(memberRequest.getPassword());
        memberRequest.setPassword(encodedPassword);

        MemberAuth authEntity = new MemberAuth(memberRequest.getAuth());
        Member memberEntity = new Member(memberRequest.getPassword(), memberRequest.getEmail(),
                memberRequest.getName(), memberRequest.getPhone(), memberRequest.getPause());
        memberEntity.addAuth(authEntity);

        repository.save(memberEntity);
    }

    @Override
    public void pause(Member member, String email) throws Exception {
        if(member.getPause()==0) {
            member.setPause(1);
        }
        else{
            member.setPause(0);
        }
        repository.save(member);
    }

    @Override
    public MemberRequest login(MemberRequest memberRequest) throws Exception {
        Optional<Member> maybeMember = repository.findByEmail(memberRequest.getEmail());

        if (maybeMember == null) {
            log.info("login(): 그런 사람 없다.");
            return null;
        }

        Member loginMember = maybeMember.get();

        if (!encoder.matches(memberRequest.getPassword(), loginMember.getPassword())) {
            log.info("login(): 비밀번호 잘못 입력하였습니다.");
            return null;
        }

        Optional<MemberAuth> maybeMemberAuth =
                authRepository.findByMemberNo(loginMember.getMemberNo());

        if (maybeMemberAuth == null) {
            log.info("auth 없음");
            return null;
        }

        MemberAuth memberAuth = maybeMemberAuth.get();
        MemberRequest response = new MemberRequest(
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                memberAuth.getAuth());

        return response;
    }

    @Override
    public boolean checkUserIdValidation(String email) throws Exception {
        Optional<Member> maybeMember = repository.findByEmail(email);

        if (maybeMember == null)
        {
            log.info("login(): 그런 사람 없다.");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkDuplicateEmail(String email) throws Exception {
        Optional<Member> checkmember = repository.findByEmail(email);
        if (checkmember.isPresent()) {
            log.info("가입가능한 아이디입니다");

            return false;
        }
        return true;

    }
}
