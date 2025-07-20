package kr.spring.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.PositionMapper;
import kr.spring.member.vo.PositionVO;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionMapper positionMapper;

    @Override
    public List<PositionVO> selectPositionList() {
        return positionMapper.selectPositionList();
    }
}
