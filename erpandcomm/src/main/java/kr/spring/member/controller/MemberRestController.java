package kr.spring.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.spring.member.email.Email;
import kr.spring.member.email.EmailSender;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberRestController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private Email email;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/modifyForm")
	public ResponseEntity<Map<String, Object>> getModifyForm(@AuthenticationPrincipal PrincipalDetails principal) {
	    Map<String, Object> mapAjax = new HashMap<String, Object>();
	    
	    // 실제 로그인한 사용자 번호 가져오기
	    long user_num = principal.getMemberVO().getUser_num();
	    
	    MemberVO member = memberService.selectMember(user_num);
	    log.debug("<<사원 정보 수정 폼 요청>> : " + member);
	    
	    if (member == null) {
	        mapAjax.put("result", "logout");
	    } else {
	        // 실제 데이터 반환
	        mapAjax.put("user_name", member.getUser_name());
	        mapAjax.put("email", member.getEmail());
	        mapAjax.put("phone", member.getPhone());
	        mapAjax.put("extension_num", member.getExtension_num());
	        mapAjax.put("result", "success");
	    }
	    
	    return new ResponseEntity<Map<String, Object>>(mapAjax, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated")
	@PostMapping("/modifyForm")
	public ResponseEntity<Map<String, Object>> submitModify(@RequestBody MemberVO memberVO, @AuthenticationPrincipal PrincipalDetails principal) {
		
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		
		try {
			// 현재 로그인한 사용자
			memberVO.setUser_num(principal.getMemberVO().getUser_num());
			
			// 정보 수정
			memberService.updateMember(memberVO);
			
			log.debug("<<사원 정보 수정>> : " + memberVO);
			mapAjax.put("result", "success");
			mapAjax.put("message", "정보가 성공적으로 수정되었습니다.");
			
		} catch (Exception e) {
			log.error("사원 정보 수정 실패: ", e);
			mapAjax.put("result", "error");
			mapAjax.put("message", "정보 수정에 실패했습니다: " + e.getMessage());
		}
		
		return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.OK);
	}
	
	// 아이디 중복 체크
//	@GetMapping("/confirmId/{id}")
//	public ResponseEntity<Map<String, String>> checkId(@PathVariable String id) {
//		
//		log.debug("<<아이디 중복 체크>> : " + id);
//		
//		Map<String, String> mapAjax = new HashMap<String, String>();
//		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("id", id);
//		MemberVO member = memberService.selectIdAndNickName(map);
//		if (id!=null) { // 아이디 체크
//			if (member!=null) {
//				// 아이디 중복
//				mapAjax.put("result", "idDuplicated");
//			} else {
//				if (!Pattern.matches("^[A-Za-z0-9]{4,14}$", id)) {
//					// 패턴 불일치
//					mapAjax.put("result", "notMatchPattern");
//				} else {
//					// 패턴 일치하면서 아이디 미중복
//					mapAjax.put("result", "idNotFound");
//				} // if
//			} // if
//		} else {
//			mapAjax.put("result", "error");
//		} // if
//		
//		return new ResponseEntity<Map<String, String>>(mapAjax, HttpStatus.OK);
//	}
//	
//	// 별명 중복 체크
//	@GetMapping("/confirmNickName/{nick_name}")
//	public ResponseEntity<Map<String, String>> checkNickName(@PathVariable String nick_name) {
//		
//		log.debug("<<별명 중복 체크>> : " + nick_name);
//		
//		Map<String, String> mapAjax = new HashMap<String, String>();
//		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("nick_name", nick_name);
//		MemberVO member = memberService.selectIdAndNickName(map);
//		if (nick_name!=null) { // 별명 체크
//			if (member!=null) {
//				// 별명 중복
//				mapAjax.put("result", "nickDuplicated");
//			} else {
//				if (!Pattern.matches("^[ㄱ-ㅎ가-힣A-Za-z0-9]{2,10}$", nick_name)) {
//					// 패턴 불일치
//					mapAjax.put("result", "notMatchPattern");
//				} else {
//					// 패턴이 일치하면서 별명 미중복
//					mapAjax.put("result", "nickNotFound");
//				} // if
//			} // if
//		} else {
//			mapAjax.put("result", "error");
//		} // if
//		
//		return new ResponseEntity<Map<String,String>>(mapAjax,HttpStatus.OK);
//	}
//	
//	// 프로필 사진 업로드
//	@PutMapping("/updateMyPhoto")
//	public ResponseEntity<Map<String, String>> updateMyPhoto(MemberVO memberVO, @AuthenticationPrincipal PrincipalDetails principal){
//		
//		log.debug("<<프로필 사진 업로드>> : {}",principal);
//		
//		Map<String, String> mapAjax = new HashMap<String, String>();
//		if (principal==null) {
//			mapAjax.put("result", "logout");
//		} else {
//			memberVO.setMem_num(principal.getMemberVO().getMem_num());
//			memberService.updateProfile(memberVO);
//			mapAjax.put("result", "success");
//		} // if
//		
//		return new ResponseEntity<Map<String, String>>(mapAjax, HttpStatus.OK);
//	}
//	
//	// 비밀번호 찾기
//	@PutMapping("/getPasswordInfo")
//	public ResponseEntity<Map<String, String>> sendEmailAction(@RequestBody MemberVO memberVO) {
//		
//		log.debug("<<비밀번호 찾기>> : {}", memberVO);
//		
//		Map<String, String> mapAjax = new HashMap<String, String>();
//		MemberVO member = memberService.selectCheckMember(memberVO.getId());
//		
//		if (member != null && member.getAuthorityOrdinal() > 1 && member.getEmail().equals(memberVO.getEmail())) {
//			// 오류를 대비해서 원래 비밀번호 저장
//			String origin_passwd = member.getPasswd();
//			// 기존 비밀번호를 임시비밀번호로 변경
//			String passwd = StringUtil.randomPassword(10);
//			member.setPasswd(passwordEncoder.encode(passwd));
//			// 변경된 임시비밀번호를 DB에 저장
//			memberService.updateRandomPassword(member);
//			
//			email.setContent("임시 비밀번호는 " + passwd + " 입니다.");
//			email.setReceiver(member.getEmail());
//			email.setSubject(member.getId() + " 님 비밀번호 찾기 메일입니다.");
//			
//			log.debug("<< 임시 비밀번호>> : {}", passwd);
//			
//			try {
//				emailSender.sendEmail(email);
//				mapAjax.put("result", "success");
//			} catch (Exception e) {
//				log.debug("<<복구 비밀번호>> : {}", origin_passwd);
//				log.error("<<비밀번호 찾기>> : {}", e.toString());
//				// 오류 발생 시 비밀번호 원상 복구
//				member.setPasswd(origin_passwd);
//				memberService.updateRandomPassword(member);
//				mapAjax.put("result", "failure");
//			} // try_catch
//		} else if (member != null && member.getAuthorityOrdinal() == UserRole.SUSPENDED.ordinal()) {
//			mapAjax.put("result", "suspended");
//		} else {
//			mapAjax.put("result", "invalidInfo");
//		} // if
//		
//		return new ResponseEntity<Map<String,String>>(mapAjax, HttpStatus.OK);
//	}

}














































