drop table  member;
--1. index.jsp���� �����մϴ�.
--2. ������ ���� admin, ��� 1234�� ����ϴ�.
--3. ����� ������ 3������ϴ�.

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
	
insert into member (id,passward,name,age,gender,email) values('admin','1234','������','29','��','qkralsrl11@naver.com');
	
update member set passward =1234 where id='newfox11';
	
select count(*) from member where id= 'admin';


--Board_Ajax ������Ʈ���� �߰�--
alter table member
add(memberfile varchar2(50));

select * from member

create table member_copy
as
select * from member

drop table member_copy;


