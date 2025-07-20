package kr.spring.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.member.vo.PositionVO;

@Mapper
public interface PositionMapper {
    List<PositionVO> selectPositionList();
}
