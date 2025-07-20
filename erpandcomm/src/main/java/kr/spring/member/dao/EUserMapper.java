package kr.spring.member.dao;

import org.apache.ibatis.annotations.Mapper;
import kr.spring.member.dto.EUserDTO;

@Mapper
public interface EUserMapper {
    void insertEUser(EUserDTO euser);
}
