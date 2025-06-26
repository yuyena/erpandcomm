-- 회원 관리
create table spmember(
	mem_num number not null,
	id varchar2(16) unique not null,
	nick_name varchar2(30) unique,
	authority varchar2(30) default 'ROLE_USER' not null,
	constraint spmember_pk primary key (mem_num)
);

create table spmember_detail(
	mem_num number not null,
	name varchar2(30) not null,
	passwd varchar2(60) not null,
	phone varchar2(15) not null,
	email varchar2(50) not null,
	zipcode varchar2(5) not null,
	address1 varchar2(120) not null,
	address2 varchar2(90) not null,
	photo blob,
	photo_name varchar2(100),
	reg_date date default sysdate not null,
	modify_date date,
	constraint spmember_detail_pk primary key (mem_num),
	constraint spmember_detail_fk foreign key (mem_num) references spmember (mem_num)
);

create sequence spmember_seq;

-- 자동 로그인(스프링 시큐리티 자동 로그인 기능 사용)
create table persistent_logins(
	series varchar2(64) primary key,
	username varchar2(64) not null,
	token varchar2(64) not null,
	last_used timestamp not null
);

-- 게시판
create table spboard(
	board_num number not null,
	category char(1) not null,
	title varchar2(90) not null,
	content clob not null,
	hit number(8) default 0 not null,
	reg_date date default sysdate not null,
	modify_date date,
	filename varchar2(400),
	ip varchar2(40) not null,
	mem_num number not null,
	constraint spboard_pk primary key (board_num),
	constraint spboard_fk foreign key (mem_num) references spmember (mem_num)
);

create sequence spboard_seq;

-- 게시판 좋아요
create table spboard_fav(
	board_num number not null,
	mem_num number not null,
	constraint fav_spboard_fk1 foreign key (board_num) references spboard (board_num),
	constraint fav_spboard_fk2 foreign key (mem_num) references spmember (mem_num)
);

-- 댓글
create table spboard_reply(
	re_num number not null,
	re_content varchar2(900) not null,
	re_date date default sysdate not null,
	re_mdate date,
	re_ip varchar2(40) not null,
	board_num number not null,
	mem_num number not null,
	constraint spboard_reply_pk primary key(re_num),
	constraint spboard_reply_fk1 foreign key (board_num) references spboard (board_num),
	constraint spboard_reply_fk2 foreign key (mem_num) references spmember (mem_num)
);

create sequence spreply_seq;

-- 게시판 댓글 좋아요
create table spreply_fav(
	re_num number not null,
	mem_num number not null,
	constraint refav_spreply_fk1 foreign key (re_num) references spboard_reply (re_num),
	constraint refav_spreply_fk2 foreign key (mem_num) references spmember (mem_num)
);

-- 대댓
create table spboard_response(
	te_num number not null,
	te_content varchar2(900) not null,
	te_date date default sysdate not null,
	te_mdate date,
	te_parent_num number not null, -- 부모글 번호가 들어감(대댓글 사이에서), 자식글이 아니라 부모글일 경우 0
	te_depth number not null, -- 부모글은0 자식글은1 자식글의 자식글은2
	te_ip varchar2(40) not null,
	re_num number not null,
	mem_num number not null,
	constraint spboard_res_pk primary key (te_num),
	constraint spboard_res_fk1 foreign key (re_num) references spboard_reply (re_num),
	constraint spboard_res_fk2 foreign key (mem_num) references spmember (mem_num)
);

create sequence response_seq;













































