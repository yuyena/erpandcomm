package kr.spring.member.dao;

import org.apache.ibatis.annotations.Mapper;
import kr.spring.member.dto.EUserDetailDTO;

@Mapper
public interface EUserDetailMapper {
    void insertEUserDetail(EUserDetailDTO detail);
}
