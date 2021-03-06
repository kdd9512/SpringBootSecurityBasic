package com.springbootsecuritybasic.controller;


import com.springbootsecuritybasic.security.dto.SecAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SecurityController {

    // 로그인 없이 접근. 모든 사용자용
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void all(){
        log.info("==================all====================");
    }

    // 접근위해 로그인 필요. 회원용.
    // 로그인 한 사용자의 정보 확인을 위해 @AuthenticationPrincipal annotation 사용하여 파라메터로 받아 출력한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/member")

    // 특정한 멤버만 접근 가능하게 설정하고 싶다면 이하와 같이 설정한다.
//    @PreAuthorize("#authMember != null && #authMember.username eq \"멤버명\"")
    public void exMember(@AuthenticationPrincipal SecAuthMemberDTO authMember){
        log.info("exMember--------------------------------------------");
        log.info("===========================================================");

        log.info("Member Info : " + authMember);
    }

    // 관리권한이 있는 회원만 접근. 관리자용.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void admin(){
        log.info("==================admin====================");
    }


}
