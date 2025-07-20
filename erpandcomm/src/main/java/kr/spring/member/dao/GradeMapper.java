package kr.spring.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.member.vo.GradeVO;

@Mapper
public interface GradeMapper {
    List<GradeVO> selectGradeList();
}
