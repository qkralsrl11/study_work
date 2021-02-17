drop table comm cascade constraints;

create table comm(
num			number			primary key,
id			varchar2(30)	references member(id),
content		varchar2(200),
reg_date	date,
comment_board_num	number	references board(board_num) on delete cascade, --comm 테이블이 참조하는 보드 글 번호
comment_re_lev	number(1)	check(comment_re_lev in (0,1,2)),		--원문글이면 0 답글이면1 대댓글이면 2
comment_re_seq	number, --원문이면0, 1레벨이면 1레벨 시퀀스 +1
comment_re_ref	number	--댓글 원문 글번호
);

--게시판 글 삭제되면 참조하는 댓글도 삭제됩니다.

drop sequence comm_seq;

--t시퀀스를 생성합니다.
create sequence comm_seq;

select nvl(max(num),0)+1 from comm

insert into comm values(comm_seq.nextval(), 'admin', '싫어요', sysdate, 1, 0, comm_seq.nextval()+1 , comm_seq.nextval());

select * from comm;

select comment_re_ref, comment_re_lev, comment_re_seq from comm where num = 2;