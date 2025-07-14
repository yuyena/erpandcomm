package kr.spring.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.DepartmentVO;

@Mapper
public interface DepartmentMapper {
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
    @Select("SELECT COUNT(*) FROM department WHERE department_code = #{department_code}")
    Integer checkDepartmentCode(String department_code);
    
    // 부서명 중복 체크
    @Select("SELECT COUNT(*) FROM department WHERE department_name = #{department_name}")
    Integer checkDepartmentName(String department_name);
    
    // 부서장 지정
    @Update("UPDATE department SET manager_num = #{managerNum} WHERE department_num = #{departmentNum}")
    void setDepartmentHead(@Param("departmentNum") Long departmentNum, @Param("managerNum") Long managerNum);
    
    // 부서장 해제
    @Update("UPDATE department SET manager_num = NULL WHERE department_num = #{departmentNum}")
    void unsetDepartmentHead(@Param("departmentNum") Long departmentNum);
    
    // 부서의 부서장 조회
    @Select("SELECT manager_num FROM department WHERE department_num = #{departmentNum}")
    Long getDepartmentHead(@Param("departmentNum") Long departmentNum);
}
