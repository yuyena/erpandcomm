package kr.spring.calendar.dao;

import kr.spring.calendar.vo.CalendarVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.Date;
import java.util.List;

@Mapper
public interface CalendarMapper {
    List<CalendarVO> selectTodaySchedules(@Param("ownerId") long ownerId, @Param("today") Date today);
    int insertCalendar(CalendarVO vo);
    int updateCalendar(CalendarVO vo);
    int deleteCalendar(@Param("calendar_id") long calendar_id);
    CalendarVO selectCalendarById(@Param("calendar_id") long calendar_id);
    List<CalendarVO> selectList(@Param("ownerId") long ownerId);
    List<CalendarVO> selectMonthSchedules(@Param("ownerId") long ownerId, @Param("monthStart") Date monthStart, @Param("monthEnd") Date monthEnd);
} 