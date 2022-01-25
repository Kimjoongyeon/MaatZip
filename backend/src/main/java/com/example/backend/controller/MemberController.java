package com.example.backend.controller;

//import com.example.backend.controller.session.UserInfo;
import com.example.backend.request.MemberRequest;
import com.example.backend.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.awt.SystemColor.info;

@Controller
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService service;

    //private UserInfo info;

    private HttpSession session;

    @PostMapping("/register")
    public Object Register(
            @Validated @RequestBody MemberRequest memberRequest) throws Exception {
        log.info("jpaRegister(): " + memberRequest.getEmail() + ", " + memberRequest.getPassword() + ", " +
                (memberRequest.getAuth().equals("사업자") ? "ROLE_BUSINESS" : "ROLE_INDIVIDUAL"));

        boolean checkId = false;
        checkId = service.checkDuplicateEmail(memberRequest.getEmail());

        if (checkId == true) {
            log.info("success");
            log.info(memberRequest.getEmail());
            service.register(memberRequest);
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        } else {
            log.info("duuplicate");
            log.info(memberRequest.getEmail());
            return false;
        }

    }

    @PostMapping("/login")//ResponseEntity<UserInfo>
    public ResponseEntity jpaLogin(
            @RequestBody MemberRequest memberRequest,
            HttpServletRequest request
    ) throws Exception {

        log.info("jpaLogin() - userId: " + memberRequest.getEmail() + ", password: " + memberRequest.getPassword());

        MemberRequest memberResponse = service.login(memberRequest);

        /*
        if (!memberResponse.equals(null)) {
            log.info("Login Success");
            // 세션 할당
            info = new UserInfo();
            info.setEmail(memberResponse.getEmail());
            info.setAuth(memberResponse.getAuth());
            log.info("Session Info: " + info);

            session = request.getSession();
            session.setAttribute("member", info);
        } else {
            log.info("Login Failure");
            info = null;
        }

        return new ResponseEntity<UserInfo>(info, HttpStatus.OK);
    */
        return new ResponseEntity(info, HttpStatus.OK);
    }
}
