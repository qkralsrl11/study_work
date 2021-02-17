drop table comm cascade constraints;

create table comm(
num			number			primary key,
id			varchar2(30)	references member(id),
content		varchar2(200),
reg_date	date,
comment_board_num	number	references board(board_num) on delete cascade, --comm ���̺��� �����ϴ� ���� �� ��ȣ
comment_re_lev	number(1)	check(comment_re_lev in (0,1,2)),		--�������̸� 0 ����̸�1 �����̸� 2
comment_re_seq	number, --�����̸�0, 1�����̸� 1���� ������ +1
comment_re_ref	number	--��� ���� �۹�ȣ
);

--�Խ��� �� �����Ǹ� �����ϴ� ��۵� �����˴ϴ�.

drop sequence comm_seq;

--t�������� �����մϴ�.
create sequence comm_seq;

select nvl(max(num),0)+1 from comm

insert into comm values(comm_seq.nextval(), 'admin', '�Ⱦ��', sysdate, 1, 0, comm_seq.nextval()+1 , comm_seq.nextval());

select * from comm;

select comment_re_ref, comment_re_lev, comment_re_seq from comm where num = 2;