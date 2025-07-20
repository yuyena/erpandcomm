package kr.spring.member.service;

import java.util.List;

import kr.spring.member.vo.PositionVO;

public interface PositionService {
    List<PositionVO> selectPositionList();
}
