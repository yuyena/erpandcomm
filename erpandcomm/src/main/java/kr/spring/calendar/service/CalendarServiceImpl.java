package kr.spring.calendar.service;

import kr.spring.calendar.dao.CalendarMapper;
import kr.spring.calendar.vo.CalendarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    private CalendarMapper calendarMapper;

    @Override
    public List<CalendarVO> getTodaySchedules(long ownerId, Date today) {
        return calendarMapper.selectTodaySchedules(ownerId, today);
    }
    @Override
    public int insertCalendar(CalendarVO vo) { return calendarMapper.insertCalendar(vo); }
    @Override
    public int updateCalendar(CalendarVO vo) { return calendarMapper.updateCalendar(vo); }
    @Override
    public int deleteCalendar(long calendar_id) { return calendarMapper.deleteCalendar(calendar_id); }
    @Override
    public CalendarVO selectCalendarById(long calendar_id) { return calendarMapper.selectCalendarById(calendar_id); }
    @Override
    public List<CalendarVO> selectList(long ownerId) { return calendarMapper.selectList(ownerId); }
    @Override
    public List<CalendarVO> selectMonthSchedules(long ownerId, Date monthStart, Date monthEnd) { return calendarMapper.selectMonthSchedules(ownerId, monthStart, monthEnd); }
} 