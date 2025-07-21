package kr.spring.calendar.vo;

import lombok.Data;
import java.util.Date;

@Data
public class CalendarVO {
    private long calendar_id;
    private String title;
    private String description;
    private long owner_id;
    private String is_public;
    private Date created_at;
    private Date updated_at;
} 