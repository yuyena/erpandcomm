package kr.spring.member.service;

import java.util.List;

import kr.spring.member.vo.GradeVO;

public interface GradeService {
    List<GradeVO> selectGradeList();
}
