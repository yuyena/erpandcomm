package kr.spring.calendar.service;

import kr.spring.calendar.vo.CalendarVO;
import java.util.Date;
import java.util.List;

public interface CalendarService {
    List<CalendarVO> getTodaySchedules(long ownerId, Date today);
    int insertCalendar(CalendarVO vo);
    int updateCalendar(CalendarVO vo);
    int deleteCalendar(long calendar_id);
    CalendarVO selectCalendarById(long calendar_id);
    List<CalendarVO> selectList(long ownerId);
    List<CalendarVO> selectMonthSchedules(long ownerId, Date monthStart, Date monthEnd);
} 