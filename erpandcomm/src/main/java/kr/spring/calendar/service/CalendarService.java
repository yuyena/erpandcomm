package kr.spring.calendar.service;

import kr.spring.calendar.vo.CalendarVO;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;

public interface CalendarService {
    List<CalendarVO> getTodaySchedules(long ownerId, Date today);
    int insertCalendar(CalendarVO vo);
    int updateCalendar(CalendarVO vo);
    int deleteCalendar(long calendar_id);
    CalendarVO selectCalendarById(long calendar_id);
    List<CalendarVO> selectList(long ownerId);
    List<CalendarVO> selectMonthSchedules(long ownerId, LocalDate monthStart, LocalDate monthEnd);
} 