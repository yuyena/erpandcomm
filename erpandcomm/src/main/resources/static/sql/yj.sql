-- payroll 쿼리
CREATE TABLE payroll (
	payroll_id  NUMBER PRIMARY KEY,
	salary_month VARCHAR2(7) NOT NULL,
	salary_date String NOT NULL,
	emp_id NUMBER NOT NULL,
    base_salary      NUMBER(12, 2) ,      -- 기본급
    bonus            NUMBER(12, 2) ,      -- 상여금
    deductions       NUMBER(12, 2) ,      -- 공제액
    net_pay          NUMBER(12, 2) ,      -- 실지급액
    status           VARCHAR2(20)  ,          -- 상태값: paid, pending, canceled 등
    created_at       DATE          SYSDATE,
    updated_at       DATE          SYSDATE,
    CONSTRAINT fk_payroll_emp FOREIGN KEY (emp_id) REFERENCES euser_detail(user_num)
)

CREATE SEQUENCE payroll_seq;