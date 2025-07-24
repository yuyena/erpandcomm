package kr.spring.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personnel")
public class PersonnelController {
    
	
    @GetMapping("/department")
    public String department() {
        return "views/personnel/department";
    }
    
    @GetMapping("/employee")
    public String employee() {
        return "views/personnel/employee";
    }
    
    @GetMapping("/employee/register")
    public String employeeRegister() {
        return "views/personnel/employee_register";
    }
    
    @GetMapping("/employee/{employeeId}/edit")
    public String employeeEdit() {
        return "views/personnel/employee_edit";
    }
    
    @GetMapping("/attendance")
    public String attendance() {
        return "views/personnel/attendanceList";
    }
    
    @GetMapping("/vacation")
    public String vacation() {
        return "views/personnel/vacationList";
    }
    
    
    @GetMapping("/salary")
    public String salary() {
        return "views/personnel/salaryList";
    }
    
    @GetMapping("/evaluation")
    public String evaluation() {
        return "views/personnel/evaluation";
    }
    
    
    @GetMapping("/statistics")
    public String statistics() {
        return "views/personnel/statistics";
    }
}
