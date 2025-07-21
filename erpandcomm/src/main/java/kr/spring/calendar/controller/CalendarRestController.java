package kr.spring.calendar.controller;

import kr.spring.calendar.service.CalendarService;
import kr.spring.calendar.vo.CalendarVO;
import kr.spring.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/calendar")
public class CalendarRestController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/today")
    public List<CalendarVO> getTodaySchedules(HttpSession session) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        if (loginMember == null) return List.of();
        long ownerId = loginMember.getUser_num();
        Date today = new Date();
        return calendarService.getTodaySchedules(ownerId, today);
    }

    @GetMapping("/list")
    public List<CalendarVO> getList(HttpSession session) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        if (loginMember == null) return List.of();
        return calendarService.selectList(loginMember.getUser_num());
    }

    @GetMapping("/month")
    public List<CalendarVO> getMonthSchedules(HttpSession session,
        @RequestParam("monthStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthStart,
        @RequestParam("monthEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthEnd) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        if (loginMember == null) return List.of();
        return calendarService.selectMonthSchedules(loginMember.getUser_num(), monthStart, monthEnd);
    }

    @GetMapping("/{calendar_id}")
    public CalendarVO getCalendar(@PathVariable("calendar_id") long calendar_id) {
        return calendarService.selectCalendarById(calendar_id);
    }

    @PostMapping("/add")
    public int addCalendar(@RequestBody CalendarVO vo, HttpSession session) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        if (loginMember == null) return 0;
        vo.setOwner_id(loginMember.getUser_num());
        if (vo.getCreated_at() == null) {
            vo.setCreated_at(new Date());
        }
        return calendarService.insertCalendar(vo);
    }

    @PutMapping("/update")
    public int updateCalendar(@RequestBody CalendarVO vo) {
        return calendarService.updateCalendar(vo);
    }

    @DeleteMapping("/delete/{calendar_id}")
    public int deleteCalendar(@PathVariable("calendar_id") long calendar_id) {
        return calendarService.deleteCalendar(calendar_id);
    }
} 