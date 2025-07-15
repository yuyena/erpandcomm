package kr.spring.member.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalMemberInfoAdvice {
    @ModelAttribute("loginMember")
    public MemberVO loginMember(@AuthenticationPrincipal PrincipalDetails principal) {
        return principal != null ? principal.getMemberVO() : null;
    }

    @ModelAttribute("gradeMap")
    public Map<Integer, String> gradeMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "사원");
        map.put(2, "주임");
        map.put(3, "대리");
        map.put(4, "과장");
        map.put(5, "차장");
        map.put(6, "부장");
        map.put(7, "임원");
        return map;
    }

    @ModelAttribute("positionMap")
    public Map<Long, String> positionMap() {
        Map<Long, String> map = new HashMap<>();
        map.put(1L, "사원");
        map.put(2L, "주임");
        map.put(3L, "대리");
        map.put(4L, "과장");
        map.put(5L, "차장");
        return map;
    }
} 