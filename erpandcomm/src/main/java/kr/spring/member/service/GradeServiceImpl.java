package kr.spring.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.GradeMapper;
import kr.spring.member.vo.GradeVO;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public List<GradeVO> selectGradeList() {
        return gradeMapper.selectGradeList();
    }
}
