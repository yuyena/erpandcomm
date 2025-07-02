-- 인사관리 시스템 테이블 정의

-- 🔢 기본 인사 정보
CREATE TABLE euser (
    user_num       NUMBER          PRIMARY KEY,
    employee_code  VARCHAR2(12)    NOT NULL UNIQUE,
    authority      VARCHAR2(30)    NOT NULL
);

CREATE TABLE euser_detail (
    user_num          NUMBER           PRIMARY KEY,
    user_name         VARCHAR2(10)    NOT NULL,
    passwd            VARCHAR2(60)    NOT NULL,
    phone             VARCHAR2(15)    NOT NULL,
    email             VARCHAR2(50)    NOT NULL,
    photo             VARCHAR2(400),
    hire_date         DATE            DEFAULT SYSDATE NOT NULL,
    resignation_date  DATE,
    salary            NUMBER,
    department_num    NUMBER          NOT NULL,
    position_num      NUMBER          NOT NULL,
    grade_num         NUMBER          NOT NULL,
    CONSTRAINT fk_user_detail_user
        FOREIGN KEY (user_num)
        REFERENCES euser(user_num),
    CONSTRAINT fk_user_detail_dept
        FOREIGN KEY (department_num)
        REFERENCES department(department_num),
    CONSTRAINT fk_user_detail_pos
        FOREIGN KEY (position_num)
        REFERENCES position(position_num),
    CONSTRAINT fk_user_detail_grade
        FOREIGN KEY (grade_num)
        REFERENCES grade(grade_num)
);

-- 🔢 조직 구조
CREATE TABLE department (
    department_num  NUMBER          PRIMARY KEY,
    department_name VARCHAR2(10)    NOT NULL,
    manager_num     NUMBER          NOT NULL,
    CONSTRAINT fk_department_manager
        FOREIGN KEY (manager_num)
        REFERENCES euser(user_num)
);

CREATE TABLE position (
    position_num   NUMBER          PRIMARY KEY,
    position_name  VARCHAR2(10)    NOT NULL,
    position_code  VARCHAR2(5)     NOT NULL
);

CREATE TABLE grade (
    grade_num   NUMBER          PRIMARY KEY,
    grade_name  VARCHAR2(10)    NOT NULL,
    grade_code  VARCHAR2(5)     NOT NULL
);

-- 🔢 근태 관리
CREATE TABLE attendance (
    attendance_id        NUMBER        PRIMARY KEY,
    emp_id               NUMBER        NOT NULL,
    work_date            DATE          NOT NULL,
    check_in_time        DATE,
    check_out_time       DATE,
    scheduled_in_time    DATE          NOT NULL,
    scheduled_out_time   DATE          NOT NULL,
    work_hours           NUMBER(4,2),
    overtime_hours       NUMBER(4,2),
    late_minutes         NUMBER,
    early_leave_minutes  NUMBER,
    break_time_minutes   NUMBER,
    status               VARCHAR2(20)  NOT NULL,
    work_type            VARCHAR2(20)  NOT NULL,
    location             VARCHAR2(100),
    ip_address           VARCHAR2(45),
    device_info          VARCHAR2(200),
    notes                VARCHAR2(500),
    approved_by          NUMBER,
    approved_at          DATE,
    created_at           DATE          NOT NULL,
    updated_at           DATE          NOT NULL,
    CONSTRAINT fk_attendance_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_attendance_approver FOREIGN KEY (approved_by) REFERENCES euser(user_num)
);

-- 🔢 휴가 관리
CREATE TABLE vacation_request (
    request_id           NUMBER         PRIMARY KEY,
    emp_id               NUMBER         NOT NULL,
    vacation_type        VARCHAR2(30)   NOT NULL,
    start_date           DATE           NOT NULL,
    end_date             DATE           NOT NULL,
    start_time           VARCHAR2(5),
    end_time             VARCHAR2(5),
    total_days           NUMBER(3,1)    NOT NULL,
    reason               VARCHAR2(500)  NOT NULL,
    emergency_contact    VARCHAR2(100),
    replacement_emp_id   NUMBER,
    status               VARCHAR2(20)   NOT NULL,
    request_date         DATE           NOT NULL,
    approved_by          NUMBER,
    approved_at          DATE,
    reject_reason        VARCHAR2(500),
    attachment_url       VARCHAR2(500),
    created_at           DATE           NOT NULL,
    updated_at           DATE           NOT NULL,
    CONSTRAINT fk_vacation_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_vacation_replacement FOREIGN KEY (replacement_emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_vacation_approver FOREIGN KEY (approved_by) REFERENCES euser(user_num)
);

-- 🔢 연장 근무 관리
CREATE TABLE overtime_request (
    overtime_id       NUMBER          PRIMARY KEY,
    emp_id            NUMBER          NOT NULL,
    work_date         DATE            NOT NULL,
    start_time        VARCHAR2(5)     NOT NULL,
    end_time          VARCHAR2(5)     NOT NULL,
    planned_hours     NUMBER(3,1)     NOT NULL,
    actual_hours      NUMBER(3,1),
    reason            VARCHAR2(500)   NOT NULL,
    work_content      VARCHAR2(1000),
    is_holiday_work   NUMBER(1)       NOT NULL,
    meal_provided     NUMBER(1)       NOT NULL,
    status            VARCHAR2(20)    NOT NULL,
    request_date      DATE            NOT NULL,
    approved_by       NUMBER,
    approved_at       DATE,
    reject_reason     VARCHAR2(500),
    created_at        DATE            NOT NULL,
    updated_at        DATE            NOT NULL,
    CONSTRAINT fk_overtime_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_overtime_approver FOREIGN KEY (approved_by) REFERENCES euser(user_num)
);

-- 🔢 시퀀스 생성
CREATE SEQUENCE euser_seq;
CREATE SEQUENCE department_seq;
CREATE SEQUENCE position_seq;
CREATE SEQUENCE grade_seq;
CREATE SEQUENCE attendance_seq;
CREATE SEQUENCE vacation_request_seq;
CREATE SEQUENCE overtime_request_seq;
