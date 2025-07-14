package kr.spring.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.spring.chat.service.ChatService;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.member.vo.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatRestContoller {
    @Autowired
    private ChatService chatService;

    @PostMapping("/notActiveRoom")
    public ResponseEntity<Map<String, Object>> deleteRoom(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam("room_num") Long roomNum) {

        Map<String, Object> mapAjax = new HashMap<String, Object>();

        if (principal == null || principal.getMemberVO() == null) {
        	mapAjax.put("result", "logout");
        }

        long myUserNum = principal.getMemberVO().getUser_num();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_num", roomNum);
        List<ChatMemberVO> memberList = chatService.selectMember(map);

        ChatMemberVO owner = null;
        for (ChatMemberVO member : memberList) {
            if ("방장".equals(member.getRole())) {
                owner = member;
                break;
            }
        }
        
        if (owner != null && myUserNum == owner.getUser_num()) {
            chatService.notActive(roomNum);
            mapAjax.put("result", "success");
        } else {
        	mapAjax.put("result", "wrongAccess");
        }
        
        return new ResponseEntity<Map<String, Object>>(mapAjax, HttpStatus.OK);
    }
}










































