CREATE TABLE work_schedule (
    schedule_id       NUMBER         PRIMARY KEY,
    emp_id            NUMBER         NOT NULL,
    work_date         DATE           NOT NULL,
    shift_type        VARCHAR2(20)   NOT NULL,
    start_time        VARCHAR2(5)    NOT NULL,
    end_time          VARCHAR2(5)    NOT NULL,
    break_minutes     NUMBER         NOT NULL,
    work_hours        NUMBER(3,1)    NOT NULL,
    location          VARCHAR2(100),
    is_holiday        NUMBER(1)      NOT NULL,
    is_overtime       NUMBER(1)      NOT NULL,
    notes             VARCHAR2(500),
    created_by        NUMBER,
    created_at        DATE           NOT NULL,
    updated_at        DATE           NOT NULL,
    CONSTRAINT fk_schedule_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_schedule_creator FOREIGN KEY (created_by) REFERENCES euser(user_num)
);
CREATE TABLE chat_room (
    room_num      NUMBER          PRIMARY KEY,
    room_name     VARCHAR2(100)   NOT NULL,
    room_type     VARCHAR2(20)    NOT NULL,  -- 예: '1:1', 'group'
    description   VARCHAR2(1000),
    created_by    NUMBER          NOT NULL,
    max_members   NUMBER,
    is_active     CHAR(1)         DEFAULT 'Y' NOT NULL,  -- 'Y' 또는 'N'
    created_at    DATE            DEFAULT SYSDATE NOT NULL,
    updated_at    DATE,
    CONSTRAINT fk_chatroom_creator FOREIGN KEY (created_by) REFERENCES euser(user_num)
);

CREATE TABLE chat_message (
    message_num   NUMBER          PRIMARY KEY,
    room_num      NUMBER          NOT NULL,
    sender_num    NUMBER          NOT NULL,
    content       VARCHAR2(1000),
    sent_at    DATE            NOT NULL,
    CONSTRAINT fk_message_room FOREIGN KEY (room_num) REFERENCES chat_room(room_num),
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_num) REFERENCES euser(user_num)
);

CREATE TABLE chat_member (
    member_num  NUMBER          PRIMARY KEY,
    room_num    NUMBER          NOT NULL,
    user_num    NUMBER          NOT NULL,
    role        VARCHAR2(20)    NOT NULL,     -- 예: '방장', '멤버'
    joined_at   DATE            DEFAULT SYSDATE NOT NULL,
    is_active   CHAR(1)         DEFAULT 'Y' NOT NULL,  -- 'Y'/'N'
    CONSTRAINT fk_chatmember_room FOREIGN KEY (room_num) REFERENCES chat_room(room_num),
    CONSTRAINT fk_chatmember_user FOREIGN KEY (user_num) REFERENCES euser(user_num)
);

CREATE TABLE chat_message_read (
    message_num   NUMBER      NOT NULL,  -- 읽은 메시지ID (FK)
    user_num      NUMBER      NOT NULL,  -- 읽은 사용자ID (FK)
    read_at       DATE        NOT NULL,  -- 읽은 시각
    CONSTRAINT pk_message_read PRIMARY KEY (message_num, user_num),
    CONSTRAINT fk_read_message FOREIGN KEY (message_num) REFERENCES chat_message(message_num),
    CONSTRAINT fk_read_user FOREIGN KEY (user_num) REFERENCES euser(user_num)
);

CREATE TABLE calendar (
    calendar_id    NUMBER          PRIMARY KEY,
    title          VARCHAR2(200)   NOT NULL,
    description    CLOB,
    owner_id       NUMBER          NOT NULL,
    is_public      CHAR(1)         NOT NULL,
    created_at     DATE            NOT NULL,
    updated_at     DATE,
    CONSTRAINT fk_calendar_owner FOREIGN KEY (owner_id) REFERENCES euser(user_num)
);

CREATE TABLE event (
    event_id       NUMBER          PRIMARY KEY,
    calendar_id    NUMBER          NOT NULL,
    title          VARCHAR2(200)   NOT NULL,
    description    VARCHAR2(1000),
    start_date     DATE            NOT NULL,
    end_date       DATE            NOT NULL,
    location       VARCHAR2(255),
    status         VARCHAR2(20)    NOT NULL,
    created_by     NUMBER          NOT NULL,
    created_at     DATE            NOT NULL,
    updated_at     DATE,
    CONSTRAINT fk_event_calendar FOREIGN KEY (calendar_id) REFERENCES calendar(calendar_id),
    CONSTRAINT fk_event_creator FOREIGN KEY (created_by) REFERENCES euser(user_num)
);

CREATE TABLE event_attendee (
    attendee_id    NUMBER          PRIMARY KEY,
    event_id       NUMBER          NOT NULL,
    employee_code  VARCHAR2(12)        NOT NULL,
    status         VARCHAR2(20)    NOT NULL,
    created_at     DATE            NOT NULL,
    CONSTRAINT fk_attendee_event FOREIGN KEY (event_id) REFERENCES event(event_id),
    CONSTRAINT fk_attendee_user FOREIGN KEY (employee_code) REFERENCES euser(employee_code)
);

CREATE TABLE notice (
    noti_num      NUMBER          PRIMARY KEY,
    noti_title    VARCHAR2(100)   NOT NULL,
    noti_content  VARCHAR2(2000)  NOT NULL,
    noti_date     DATE            DEFAULT SYSDATE NOT NULL
);


-- 💬 채팅 관련
CREATE SEQUENCE chat_room_seq;
CREATE SEQUENCE chat_message_seq;
CREATE SEQUENCE chat_member_seq;

-- 📅 일정/캘린더
CREATE SEQUENCE calendar_seq;
CREATE SEQUENCE event_seq;
CREATE SEQUENCE event_attendee_seq;

-- 📢 공지사항
CREATE SEQUENCE notice_seq;