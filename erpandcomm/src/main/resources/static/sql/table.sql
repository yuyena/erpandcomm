CREATE TABLE euser (
    user_num       NUMBER          PRIMARY KEY,
    employee_code  VARCHAR2(12)    NOT NULL UNIQUE,
    auth           NUMBER(1)       NOT NULL
);

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
CREATE TABLE euser_detail (
    user_num          NUMBER           PRIMARY KEY,
    user_name         VARCHAR2(10)     NOT NULL,
    passwd            VARCHAR2(60)     NOT NULL,
    phone             VARCHAR2(15)     NOT NULL,
    email             VARCHAR2(50)     NOT NULL,
    photo             VARCHAR2(400),
    hire_date         DATE             DEFAULT SYSDATE NOT NULL,
    resignation_date  DATE,
    salary            NUMBER,
    department_num    NUMBER           NOT NULL,
    position_num      NUMBER           NOT NULL,
    grade_num         NUMBER           NOT NULL,
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

CREATE TABLE vacation_balance (
    balance_id        NUMBER         PRIMARY KEY,
    emp_id            NUMBER         NOT NULL,
    year              NUMBER(4)      NOT NULL,
    vacation_type     VARCHAR2(30)   NOT NULL,
    total_days        NUMBER(4,1)    NOT NULL,
    used_days         NUMBER(4,1)    NOT NULL,
    remaining_days    NUMBER(4,1)    NOT NULL,
    carry_over_days   NUMBER(4,1)    NOT NULL,
    expire_date       DATE,
    notes             VARCHAR2(500),
    created_at        DATE           NOT NULL,
    updated_at        DATE           NOT NULL,
    CONSTRAINT fk_balance_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num)
);

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

CREATE TABLE attendance_summary (
    summary_id             NUMBER        PRIMARY KEY,
    emp_id                 NUMBER        NOT NULL,
    year                   NUMBER(4)     NOT NULL,
    month                  NUMBER(2)     NOT NULL,
    total_work_days        NUMBER        NOT NULL,
    total_work_hours       NUMBER(5,1)   NOT NULL,
    regular_hours          NUMBER(5,1)   NOT NULL,
    overtime_hours         NUMBER(5,1)   NOT NULL,
    night_hours            NUMBER(5,1)   NOT NULL,
    holiday_hours          NUMBER(5,1)   NOT NULL,
    late_count             NUMBER        NOT NULL,
    early_leave_count      NUMBER        NOT NULL,
    absent_count           NUMBER        NOT NULL,
    vacation_days          NUMBER(3,1)   NOT NULL,
    sick_days              NUMBER(3,1)   NOT NULL,
    total_late_minutes     NUMBER        NOT NULL,
    total_early_minutes    NUMBER        NOT NULL,
    is_finalized           NUMBER(1)     NOT NULL,
    finalized_by           NUMBER,
    finalized_at           DATE,
    created_at             DATE          NOT NULL,
    updated_at             DATE          NOT NULL,
    CONSTRAINT fk_summary_emp FOREIGN KEY (emp_id) REFERENCES euser(user_num),
    CONSTRAINT fk_summary_finalizer FOREIGN KEY (finalized_by) REFERENCES euser(user_num)
);

CREATE TABLE product (
    product_num    NUMBER          PRIMARY KEY,
    category_num   NUMBER          NOT NULL,
    product_name   VARCHAR2(100)   NOT NULL,
    status         NUMBER          NOT NULL,
    unit_pricce    NUMBER(10,2)    NOT NULL,
    unit           VARCHAR2(10)    NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_num) REFERENCES product_category(category_num)
);

CREATE TABLE product_category (
    category_num   NUMBER          PRIMARY KEY,
    category_name  VARCHAR2(50)    NOT NULL
);


CREATE TABLE stock_movements (
    movement_num   NUMBER          PRIMARY KEY,
    product_num   NUMBER          NOT NULL,
    emp_num        NUMBER          NOT NULL,
    movement_type  NUMBER          NOT NULL,
    quantity       NUMBER          NOT NULL,
    movement_date  DATE            DEFAULT SYSDATE NOT NULL,
    note           VARCHAR2(200),
    CONSTRAINT fk_stock_product FOREIGN KEY (product_num) REFERENCES product(product_num),
    CONSTRAINT fk_stock_emp FOREIGN KEY (emp_num) REFERENCES euser(user_num)
);


CREATE TABLE client (
    client_num       NUMBER         PRIMARY KEY,
    client_name      VARCHAR2(100)  NOT NULL,
    client_type      NUMBER         NOT NULL,
    contact_person   VARCHAR2(50)   NOT NULL,
    phone            VARCHAR2(20),
    email            VARCHAR2(100),
    address1         VARCHAR2(100),
    address2         VARCHAR2(100)
);


CREATE TABLE puchase_order (
    purchase_order_num  NUMBER       PRIMARY KEY,
    supplier_num        NUMBER       NOT NULL,
    order_date          DATE         DEFAULT SYSDATE NOT NULL,
    total_price         NUMBER(15,2) NOT NULL,
    emp_num             NUMBER       NOT NULL,
    CONSTRAINT fk_purchase_supplier FOREIGN KEY (supplier_num) REFERENCES client(client_num),
    CONSTRAINT fk_purchase_emp FOREIGN KEY (emp_num) REFERENCES euser(user_num)
);

CREATE TABLE puchase_order_detail (
    purchase_order_num  NUMBER       NOT NULL,
    product_num         NUMBER       NOT NULL,
    quantity            NUMBER       NOT NULL,
    unit_price          NUMBER(10,2) NOT NULL,
    PRIMARY KEY (purchase_order_num, product_num),
    CONSTRAINT fk_pod_purchase FOREIGN KEY (purchase_order_num) REFERENCES puchase_order(purchase_order_num),
    CONSTRAINT fk_pod_product FOREIGN KEY (product_num) REFERENCES product(product_num)
);

CREATE TABLE sales_order (
    sales_order_num  NUMBER       PRIMARY KEY,
    customer_num     NUMBER       NOT NULL,
    order_date       DATE         DEFAULT SYSDATE NOT NULL,
    total_price      NUMBER(15,2) NOT NULL,
    emp_num          NUMBER       NOT NULL,
    CONSTRAINT fk_sales_customer FOREIGN KEY (customer_num) REFERENCES client(client_num),
    CONSTRAINT fk_sales_emp FOREIGN KEY (emp_num) REFERENCES euser(user_num)
);

CREATE TABLE sales_order_detail (
    sales_order_num  NUMBER       NOT NULL,
    product_num      NUMBER       NOT NULL,
    quantity         NUMBER       NOT NULL,
    unit_price       NUMBER(10,2) NOT NULL,
    PRIMARY KEY (sales_order_num, product_num),
    CONSTRAINT fk_sod_sale FOREIGN KEY (sales_order_num) REFERENCES sales_order(sales_order_num),
    CONSTRAINT fk_sod_product FOREIGN KEY (product_num) REFERENCES product(product_num)
);


CREATE TABLE transaction_history (
    transaction_num   NUMBER         PRIMARY KEY,
    transaction_type  NUMBER         NOT NULL,
    product_num       NUMBER         NOT NULL,
    client_num        NUMBER,
    quantity          NUMBER         NOT NULL,
    unit_price        NUMBER(10,2),
    total_amount      NUMBER(15,2),
    transaction_date  DATE           DEFAULT SYSDATE NOT NULL,
    emp_num           NUMBER,
    note              VARCHAR2(200),
    CONSTRAINT fk_transaction_product FOREIGN KEY (product_num) REFERENCES product(product_num),
    CONSTRAINT fk_transaction_client FOREIGN KEY (client_num) REFERENCES client(client_num),
    CONSTRAINT fk_transaction_emp FOREIGN KEY (emp_num) REFERENCES euser(user_num)
);

CREATE TABLE product_warehouse (
    warehouse_num     NUMBER   PRIMARY KEY, -- 창고 번호 (PK)
    warehouse_name    VARCHAR2(100)    NOT NULL, -- 창고 이름
    location          VARCHAR2(100), -- 장소
    warehouse_type NUMBER(1) DEFAULT 1 – 본사(0), 지점(1)
);


-- 🔢 회원 관련
CREATE SEQUENCE euser_seq;
CREATE SEQUENCE euser_detail_seq;

-- 🔢 부서/직책/직급
CREATE SEQUENCE department_seq;
CREATE SEQUENCE position_seq;
CREATE SEQUENCE grade_seq;

-- 🔢 근태 관련
CREATE SEQUENCE attendance_seq;
CREATE SEQUENCE vacation_request_seq;
CREATE SEQUENCE vacation_balance_seq;
CREATE SEQUENCE work_schedule_seq;
CREATE SEQUENCE overtime_request_seq;
CREATE SEQUENCE attendance_summary_seq;

-- 💬 채팅 관련
CREATE SEQUENCE chat_room_seq;
CREATE SEQUENCE chat_message_seq;
CREATE SEQUENCE chat_member_seq;

-- 📅 일정/캘린더
CREATE SEQUENCE calendar_seq;
CREATE SEQUENCE event_seq;
CREATE SEQUENCE event_attendee_seq;


-- 🛒 상품/재고
CREATE SEQUENCE product_seq;
CREATE SEQUENCE product_category_seq;
CREATE SEQUENCE stock_movement_seq;
CREATE SEQUENCE product_warehouse_seq;

-- 🤝 거래처/구매/판매
CREATE SEQUENCE client_seq;
CREATE SEQUENCE puchase_order_seq;
CREATE SEQUENCE puchase_order_detail_seq;
CREATE SEQUENCE sales_order_seq;
CREATE SEQUENCE sales_order_detail_seq;
CREATE SEQUENCE transaction_history_seq;

-- 📢 공지사항
CREATE SEQUENCE notice_seq;


