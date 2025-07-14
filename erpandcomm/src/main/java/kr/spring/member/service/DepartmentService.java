package kr.spring.member.service;

import java.util.List;
import java.util.Map;

import kr.spring.member.vo.DepartmentVO;

public interface DepartmentService {
    // 부서 등록
    void insertDepartment(DepartmentVO department);
    
    // 부서 수정
    void updateDepartment(DepartmentVO department);
    
    // 부서 삭제
    void deleteDepartment(Long department_num);
    
    // 부서 조회
    DepartmentVO selectDepartment(Long department_num);
    
    // 부서 목록 조회
    List<DepartmentVO> selectDepartmentList(Map<String, Object> map);
    
    // 부서 수 카운트
    Integer selectDepartmentCount();
    
    // 부서 코드 중복 체크
    Integer checkDepartmentCode(String department_code);
    
    // 부서명 중복 체크
    Integer checkDepartmentName(String department_name);
    
    // 부서장 지정
    void setDepartmentHead(Long departmentNum, Long managerNum);
    
    // 부서장 해제
    void unsetDepartmentHead(Long departmentNum);
    
    // 부서의 부서장 조회
    Long getDepartmentHead(Long departmentNum);
}
