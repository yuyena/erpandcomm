package kr.spring.member.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.DepartmentMapper;
import kr.spring.member.vo.DepartmentVO;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Override
    public void insertDepartment(DepartmentVO department) {
        // 부서 코드 중복 체크
        if (departmentMapper.checkDepartmentCode(department.getDepartment_code()) > 0) {
            throw new RuntimeException("이미 사용 중인 부서 코드입니다.");
        }
        
        // 부서명 중복 체크
        if (departmentMapper.checkDepartmentName(department.getDepartment_name()) > 0) {
            throw new RuntimeException("이미 사용 중인 부서명입니다.");
        }
        
        departmentMapper.insertDepartment(department);
    }
    
    @Override
    public void updateDepartment(DepartmentVO department) {
        departmentMapper.updateDepartment(department);
    }
    
    @Override
    public void deleteDepartment(Long department_num) {
        departmentMapper.deleteDepartment(department_num);
    }
    
    @Override
    public DepartmentVO selectDepartment(Long department_num) {
        return departmentMapper.selectDepartment(department_num);
    }
    
    @Override
    public List<DepartmentVO> selectDepartmentList(Map<String, Object> map) {
        return departmentMapper.selectDepartmentList(map);
    }
    
    @Override
    public Integer selectDepartmentCount() {
        return departmentMapper.selectDepartmentCount();
    }
    
    @Override
    public Integer checkDepartmentCode(String department_code) {
        return departmentMapper.checkDepartmentCode(department_code);
    }
    
    @Override
    public Integer checkDepartmentName(String department_name) {
        return departmentMapper.checkDepartmentName(department_name);
    }
    
    @Override
    public void setDepartmentHead(Long departmentNum, Long managerNum) {
        departmentMapper.setDepartmentHead(departmentNum, managerNum);
    }
    
    @Override
    public void unsetDepartmentHead(Long departmentNum) {
        departmentMapper.unsetDepartmentHead(departmentNum);
    }
    
    @Override
    public Long getDepartmentHead(Long departmentNum) {
        return departmentMapper.getDepartmentHead(departmentNum);
    }
}
