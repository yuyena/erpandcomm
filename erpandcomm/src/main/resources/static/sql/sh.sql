-- 🔹 공급업체 10개 (client_type = 0)
INSERT INTO client VALUES (1, '(주)한빛상사', 0, '김영수', '010-1234-5678', 'yskim@hanbit.com', '서울시 강남구', '테헤란로 123');
INSERT INTO client VALUES (2, '삼성물산', 0, '박지현', '010-2345-6789', 'jhpark@samsung.com', '서울시 서초구', '서초대로 45');
INSERT INTO client VALUES (3, 'LG화학', 0, '이정훈', '010-3456-7890', 'jhlee@lgchem.com', '대전시 유성구', '대덕대로 200');
INSERT INTO client VALUES (4, '현대건설', 0, '최민지', '010-4567-8901', 'mjchoi@hyundai.com', '부산시 해운대구', '센텀중앙로 55');
INSERT INTO client VALUES (5, '(주)케이텍', 0, '정우성', '010-5678-9012', 'wsjung@k-tech.com', '인천시 남동구', '논현로 87');
INSERT INTO client VALUES (6, '두산중공업', 0, '윤미래', '010-6789-0123', 'mryoon@doosan.com', '울산시 북구', '산업로 19');
INSERT INTO client VALUES (7, '롯데기공', 0, '장동건', '010-7890-1234', 'dgjang@lotte.com', '경기도 수원시', '영통구 대학로 3');
INSERT INTO client VALUES (8, '포스코엔지니어링', 0, '한예슬', '010-8901-2345', 'yshan@posco.com', '포항시 남구', '철강로 98');
INSERT INTO client VALUES (9, '(주)동서테크', 0, '서지석', '010-9012-3456', 'jsseo@dstech.com', '광주시 북구', '첨단과기로 77');
INSERT INTO client VALUES (10, 'CJ대한통운', 0, '오지호', '010-0123-4567', 'jhoo@cjlogis.com', '서울시 송파구', '문정로 12');

-- 🔹 고객 20명 (client_type = 1)
INSERT INTO client VALUES (11, '홍길동', 1, '홍길동', '010-1111-2222', 'gildong@naver.com', '서울시 마포구', '합정동 22');
INSERT INTO client VALUES (12, '이영희', 1, '이영희', '010-2222-3333', 'younghee@daum.net', '서울시 강서구', '화곡동 7');
INSERT INTO client VALUES (13, '김철수', 1, '김철수', '010-3333-4444', 'cs.kim@gmail.com', '경기도 고양시', '일산서구 중앙로 8');
INSERT INTO client VALUES (14, '박민수', 1, '박민수', '010-4444-5555', 'mspark@kakao.com', '부산시 연제구', '연산동 101');
INSERT INTO client VALUES (15, '최수정', 1, '최수정', '010-5555-6666', 'sjchoi@naver.com', '대구시 수성구', '범어로 19');
INSERT INTO client VALUES (16, '정세윤', 1, '정세윤', '010-6666-7777', 'syjeong@daum.net', '울산시 중구', '중앙로 51');
INSERT INTO client VALUES (17, '오하늘', 1, '오하늘', '010-7777-8888', 'hanooh@outlook.com', '광주시 서구', '상무대로 11');
INSERT INTO client VALUES (18, '장예은', 1, '장예은', '010-8888-9999', 'yejang@gmail.com', '서울시 종로구', '삼청로 8');
INSERT INTO client VALUES (19, '백지훈', 1, '백지훈', '010-9999-0000', 'jhbaek@hanmail.net', '경기도 용인시', '기흥구 기흥로 100');
INSERT INTO client VALUES (20, '윤가영', 1, '윤가영', '010-0000-1111', 'gyyoon@korea.com', '세종시', '나성동 세종대로 23');
INSERT INTO client VALUES (21, '김다은', 1, '김다은', '010-1234-1111', 'daunkim@naver.com', '인천시 연수구', '송도동 33');
INSERT INTO client VALUES (22, '이준호', 1, '이준호', '010-2345-2222', 'jhlee@daum.net', '대전시 서구', '둔산로 72');
INSERT INTO client VALUES (23, '한지민', 1, '한지민', '010-3456-3333', 'jminhan@gmail.com', '강원도 춘천시', '퇴계로 17');
INSERT INTO client VALUES (24, '서강준', 1, '서강준', '010-4567-4444', 'kjseo@kakao.com', '제주시', '연동 중앙로 20');
INSERT INTO client VALUES (25, '문지영', 1, '문지영', '010-5678-5555', 'jymoon@naver.com', '경기도 안양시', '동안구 시민대로 55');
INSERT INTO client VALUES (26, '노진우', 1, '노진우', '010-6789-6666', 'jwnoh@daum.net', '전주시 완산구', '서노송동 12');
INSERT INTO client VALUES (27, '배수지', 1, '배수지', '010-7890-7777', 'sjbae@gmail.com', '서울시 노원구', '공릉로 88');
INSERT INTO client VALUES (28, '유승호', 1, '유승호', '010-8901-8888', 'shyoo@outlook.com', '수원시 팔달구', '영동대로 3');
INSERT INTO client VALUES (29, '강민경', 1, '강민경', '010-9012-9999', 'mkang@naver.com', '부산시 남구', '대연동 55');
INSERT INTO client VALUES (30, '조세호', 1, '조세호', '010-0123-0000', 'sehojo@korea.com', '서울시 중랑구', '면목로 12');

-- 1. 포장/배송용품 제조 공급처
INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '한진포장산업', 0, '김지훈', '02-1234-5678', 'contact@hanjinpack.co.kr', '서울특별시 금천구 가산디지털1로 142', 'A동 805호');

INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '세방패키징', 0, '박은정', '031-9876-4321', 'info@sebangpack.com', '경기도 시흥시 공단1대로 89', '패키징센터 2층');

-- 2. 청소/위생용품 제조 공급처
INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '클린메이트산업', 0, '이정수', '032-555-7890', 'sales@cleanmate.co.kr', '인천광역시 남동구 남동대로 456', '위생동 3층');

INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '에코위생솔루션', 0, '최윤아', '02-8765-1234', 'support@ecohygiene.kr', '서울특별시 강서구 공항대로 314', '비즈니스센터 1203호');

-- 3. 사무용 가구 제조 공급처
INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '퍼스트오피스', 0, '정해성', '055-234-6789', 'first@officefurni.com', '경상남도 창원시 성산구 중앙대로 102', '3층 제조부');

INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '에이스퍼니처', 0, '홍지민', '053-789-0123', 'contact@acefurniture.co.kr', '대구광역시 달서구 이곡동 202-9', '가구제조동 1층');

-- 4. 문구류 제조 공급처
INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '모던문구산업', 0, '김가은', '042-456-7890', 'info@modernstationery.kr', '대전광역시 유성구 테크노2로 100', 'B동 2층');

INSERT INTO CLIENT (CLIENT_NUM, CLIENT_NAME, CLIENT_TYPE, CONTACT_PERSON, PHONE, EMAIL, ADDRESS1, ADDRESS2)
VALUES (CLIENT_SEQ.NEXTVAL, '에버펜코리아', 0, '오세진', '051-678-9012', 'cs@everpen.co.kr', '부산광역시 강서구 유통단지1로 55', '문구센터 4층');

