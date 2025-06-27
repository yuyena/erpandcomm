-- 직급 데이터
INSERT INTO grade (grade_num, grade_name, grade_code) 
VALUES (grade_seq.NEXTVAL, '주임', 'G03');

-- 직책 데이터  
INSERT INTO position (position_num, position_name, position_code)
VALUES (position_seq.NEXTVAL, '개발자', 'P02');

-- 사용자 기본 정보
INSERT INTO euser (user_num, employee_code, auth)
VALUES (euser_seq.NEXTVAL, 'EMP20240001', 1);

-- 부서 정보 (방금 생성한 사용자를 매니저로 설정)
INSERT INTO department (department_num, department_name, manager_num)
VALUES (department_seq.NEXTVAL, '개발팀', euser_seq.CURRVAL);

-- 사용자 상세 정보
INSERT INTO euser_detail (
    user_num,
    user_name,
    passwd,
    phone,
    email,
    photo,
    hire_date,
    resignation_date,
    salary,
    department_num,
    position_num,
    grade_num
) VALUES (
    euser_seq.CURRVAL,           -- 방금 생성한 user_num
    '김철수',
    'password123',
    '010-1234-5678',
    'kim.chulsoo@company.com',
    '/uploads/profile/kim_chulsoo.jpg',
    DATE '2024-03-01',           -- 입사일
    NULL,                        -- 퇴사일 (현재 재직중)
    4500000,                     -- 월급 450만원
    department_seq.CURRVAL,      -- 방금 생성한 부서
    position_seq.CURRVAL,        -- 방금 생성한 직책
    grade_seq.CURRVAL            -- 방금 생성한 직급
);