drop table  member;
--1. index.jsp에서 시작합니다.
--2. 관리자 계정 admin, 비번 1234를 만듭니다.
--3. 사용자 계정을 3개만듭니다.

create table member(
id 			varchar2(12),
passward 	varchar2(10),
name		varchar2(15),
age			number(2),
gender		varchar2(3),
email		varchar2(30),
PRIMARY KEY(id)
);

	
	select * from member;
	
	select * from member where id = 'admin';
	
insert into member (id,passward,name,age,gender,email) values('admin','1234','박지훈','29','남','qkralsrl11@naver.com');
	
update member set passward =1234 where id='newfox11';
	
select count(*) from member where id= 'admin';


--Board_Ajax 프로젝트에서 추가--
alter table member
add(memberfile varchar2(50));

select * from member

create table member_copy
as
select * from member

drop table member_copy;


