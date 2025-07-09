package kr.spring.attendance.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendanceFormController {

    @GetMapping("/personnel/attendanceForm")
    public String attendanceForm() {
        return "views/personnel/attendanceForm :: formContent"; // 이건 templates/personnel/attendanceForm.html 경로를 의미
    }
}
