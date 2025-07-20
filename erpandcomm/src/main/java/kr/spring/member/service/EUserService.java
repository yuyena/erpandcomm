package kr.spring.member.service;

import kr.spring.member.dto.EUserDTO;
import kr.spring.member.dto.EUserDetailDTO;

public interface EUserService {
    void registerEmployee(EUserDTO euser, EUserDetailDTO detail);
}
