package kr.spring.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.EUserDetailMapper;
import kr.spring.member.dao.EUserMapper;
import kr.spring.member.dto.EUserDTO;
import kr.spring.member.dto.EUserDetailDTO;

@Service
@Transactional
public class EUserServiceImpl implements EUserService {
    @Autowired
    private EUserMapper eUserMapper;
    @Autowired
    private EUserDetailMapper eUserDetailMapper;

    @Override
    public void registerEmployee(EUserDTO euser, EUserDetailDTO detail) {
        eUserMapper.insertEUser(euser);
        eUserDetailMapper.insertEUserDetail(detail);
    }
}
