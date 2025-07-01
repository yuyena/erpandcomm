package kr.spring.member.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.member.vo.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// 클래스 내에서 final 또는 @NonNull로 선언된 필드에 대해 생성자를 자동으로 생성
@RequiredArgsConstructor
// 로그인 시 사용자 정보를 조회하고, 이를 기반으로 인증(Authentication)을 수행하는 데 사용
@Service
public class UserSecurityService implements UserDetailsService {
	
	// @RequiredArgsConstructor에 의해 의존성 주입됨
	private final MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String employee_code) throws UsernameNotFoundException {
		
		// 임시: 새 비밀번호 생성 및 출력
	    BCryptPasswordEncoder tempEncoder = new BCryptPasswordEncoder();
	    String newEncoded = tempEncoder.encode("password123");
	    log.debug("=== 새로운 암호화된 비밀번호 (password123): {}", newEncoded);
	    log.debug("=== 이 값을 DB에 업데이트하세요 ===");
	    
		log.debug("[Spring Security Login Check1] UserSecurityService 실행");
		log.debug("[Spring Security Login Check1] 로그인 아이디 : " + employee_code);
		MemberVO member = memberService.selectCheckMember(employee_code);
		if (member==null || member.getAuthority().equals(UserRole.INACTIVE.getValue())) {
			log.debug("[Spring Security Login Check1] 로그인 아이디가 없거나 탈퇴회원");			
			throw new UsernameNotFoundException("UserNotFound");
		} // if
		log.debug("DB 비밀번호: {}", member.getPasswd());
		log.debug("입력값과 비교 결과: {}", new BCryptPasswordEncoder().matches("password123", member.getPasswd())); // ✅ 여기만 변경
		return new PrincipalDetails(member);
	}

}











































