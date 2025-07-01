-- 직급 데이터
INSERT INTO grade (grade_num, grade_name, grade_code) 
VALUES (grade_seq.NEXTVAL, '주임', 'G03');

-- 직책 데이터  
INSERT INTO position (position_num, position_name, position_code)
VALUES (position_seq.NEXTVAL, '개발자', 'P02');

-- 사용자 기본 정보
INSERT INTO euser (user_num, employee_code, authority)
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


- 1. 카테고리 테이블 먼저 생성
INSERT INTO product_category (category_num, category_name) VALUES (1, '문구류');
INSERT INTO product_category (category_num, category_name) VALUES (2, '사무용 가구');
INSERT INTO product_category (category_num, category_name) VALUES (3, '청소/위생용품');
INSERT INTO product_category (category_num, category_name) VALUES (4, '포장/배송용품');

-- 2. 상품 테이블 데이터 삽입

-- 문구류
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (1, 1, '유성볼펜 (검정)', 1, 800.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (2, 1, '겔볼펜 (파랑)', 1, 1200.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (3, 1, '사인펜 세트', 1, 15000.00, '세트');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (4, 1, 'HB 연필', 1, 500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (5, 1, '샤프펜슬 0.5mm', 1, 3000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (6, 1, '화이트보드 마커', 1, 2500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (7, 1, '형광펜 세트', 1, 8000.00, '세트');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (8, 1, '소형 스테이플러', 1, 12000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (9, 1, '대형 스테이플러', 1, 35000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (10, 1, 'A4 클리어파일', 1, 2000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (11, 1, '서류보관함', 1, 18000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_pricce, unit) VALUES (12, 1, '3공 바인더', 1, 8500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (13, 1, '메모패드', 1, 3500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (14, 1, '스프링노트 A5', 1, 4500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (15, 1, '포스트잇 소형', 1, 2800.00, '팩');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (16, 1, '포스트잇 대형', 1, 4200.00, '팩');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (17, 1, '일반클립 (소형)', 1, 800.00, '박스');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (18, 1, '대형클립', 1, 1500.00, '박스');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (19, 1, '종이컵 (커피용)', 1, 8000.00, '팩');

-- 사무용 가구
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (20, 2, '사무용 책상 1200mm', 1, 180000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (21, 2, '회의용 테이블 1800mm', 1, 350000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (22, 2, '인체공학 사무의자', 1, 280000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (23, 2, '회의용 의자', 1, 95000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (24, 2, '3단 서랍장', 1, 150000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (25, 2, '사무용 수납장', 1, 220000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (26, 2, '서류정리함', 1, 45000.00, '개');
-- 청소/위생용품
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (27, 3, '사무실용 다목적 세제', 1, 12000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (28, 3, '손세정제 500ml', 1, 8500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (29, 3, '자동디스펜서 손세정제', 1, 15000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (30, 3, '종이타월', 1, 6500.00, '팩');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (31, 3, '소형 휴지통', 1, 18000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (32, 3, '대형 휴지통', 1, 35000.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (33, 3, '데스크 클리닝 물티슈', 1, 4500.00, '팩');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (34, 3, '소형 쓰레기봉투', 1, 8000.00, '롤');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (35, 3, '대형 쓰레기봉투', 1, 12000.00, '롤');
-- 포장/배송용품
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (36, 4, '포장용 테이프', 1, 3500.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (37, 4, '마스킹 테이프', 1, 2800.00, '개');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (38, 4, 'A4 우편봉투', 1, 15000.00, '박스');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (39, 4, '대형 우편봉투', 1, 25000.00, '박스');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (40, 4, '소형 포장박스', 1, 18000.00, '묶음');
INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES (41, 4, '서류보관 박스', 0, 22000.00, '묶음');

-- 몇 개는 판매중지 상태로
UPDATE product SET status = 0 WHERE product_num IN (4, 41, 35);

COMMIT;

-- current_stock 테이블 데이터 삽입

-- 문구류 재고
INSERT INTO current_stock (product_num, current_quantity) VALUES (1, 150);   -- 유성볼펜 (검정)
INSERT INTO current_stock (product_num, current_quantity) VALUES (2, 120);   -- 겔볼펜 (파랑)
INSERT INTO current_stock (product_num, current_quantity) VALUES (3, 25);    -- 사인펜 세트
INSERT INTO current_stock (product_num, current_quantity) VALUES (4, 0);     -- HB 연필 (판매중지)
INSERT INTO current_stock (product_num, current_quantity) VALUES (5, 80);    -- 샤프펜슬 0.5mm
INSERT INTO current_stock (product_num, current_quantity) VALUES (6, 65);    -- 화이트보드 마커
INSERT INTO current_stock (product_num, current_quantity) VALUES (7, 40);    -- 형광펜 세트
INSERT INTO current_stock (product_num, current_quantity) VALUES (8, 35);    -- 소형 스테이플러
INSERT INTO current_stock (product_num, current_quantity) VALUES (9, 18);    -- 대형 스테이플러
INSERT INTO current_stock (product_num, current_quantity) VALUES (10, 200);  -- A4 클리어파일
INSERT INTO current_stock (product_num, current_quantity) VALUES (11, 22);   -- 서류보관함
INSERT INTO current_stock (product_num, current_quantity) VALUES (12, 45);   -- 3공 바인더
INSERT INTO current_stock (product_num, current_quantity) VALUES (13, 85);   -- 메모패드
INSERT INTO current_stock (product_num, current_quantity) VALUES (14, 60);   -- 스프링노트 A5
INSERT INTO current_stock (product_num, current_quantity) VALUES (15, 95);   -- 포스트잇 소형
INSERT INTO current_stock (product_num, current_quantity) VALUES (16, 75);   -- 포스트잇 대형
INSERT INTO current_stock (product_num, current_quantity) VALUES (17, 180);  -- 일반클립 (소형)
INSERT INTO current_stock (product_num, current_quantity) VALUES (18, 120);  -- 대형클립
INSERT INTO current_stock (product_num, current_quantity) VALUES (19, 55);   -- 종이컵 (커피용)

-- 사무용 가구 재고
INSERT INTO current_stock (product_num, current_quantity) VALUES (20, 8);    -- 사무용 책상 1200mm
INSERT INTO current_stock (product_num, current_quantity) VALUES (21, 5);    -- 회의용 테이블 1800mm
INSERT INTO current_stock (product_num, current_quantity) VALUES (22, 12);   -- 인체공학 사무의자
INSERT INTO current_stock (product_num, current_quantity) VALUES (23, 25);   -- 회의용 의자
INSERT INTO current_stock (product_num, current_quantity) VALUES (24, 15);   -- 3단 서랍장
INSERT INTO current_stock (product_num, current_quantity) VALUES (25, 10);   -- 사무용 수납장
INSERT INTO current_stock (product_num, current_quantity) VALUES (26, 30);   -- 서류정리함

-- 청소/위생용품 재고
INSERT INTO current_stock (product_num, current_quantity) VALUES (27, 45);   -- 사무실용 다목적 세제
INSERT INTO current_stock (product_num, current_quantity) VALUES (28, 65);   -- 손세정제 500ml
INSERT INTO current_stock (product_num, current_quantity) VALUES (29, 35);   -- 자동디스펜서 손세정제
INSERT INTO current_stock (product_num, current_quantity) VALUES (30, 80);   -- 종이타월
INSERT INTO current_stock (product_num, current_quantity) VALUES (31, 20);   -- 소형 휴지통
INSERT INTO current_stock (product_num, current_quantity) VALUES (32, 12);   -- 대형 휴지통
INSERT INTO current_stock (product_num, current_quantity) VALUES (33, 90);   -- 데스크 클리닝 물티슈
INSERT INTO current_stock (product_num, current_quantity) VALUES (34, 40);   -- 소형 쓰레기봉투
INSERT INTO current_stock (product_num, current_quantity) VALUES (35, 0);    -- 대형 쓰레기봉투 (판매중지)

-- 포장/배송용품 재고
INSERT INTO current_stock (product_num, current_quantity) VALUES (36, 110);  -- 포장용 테이프
INSERT INTO current_stock (product_num, current_quantity) VALUES (37, 85);   -- 마스킹 테이프
INSERT INTO current_stock (product_num, current_quantity) VALUES (38, 28);   -- A4 우편봉투
INSERT INTO current_stock (product_num, current_quantity) VALUES (39, 15);   -- 대형 우편봉투
INSERT INTO current_stock (product_num, current_quantity) VALUES (40, 22);   -- 소형 포장박스
INSERT INTO current_stock (product_num, current_quantity) VALUES (41, 0);    -- 서류보관 박스 (판매중지)

COMMIT;