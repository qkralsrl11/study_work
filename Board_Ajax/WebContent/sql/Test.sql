select * from emp;

create table eee(
id varchar2(12),
pass varchar2(10)
);

select * from eee;

drop table eee;

select * from dept;

update dept set loc='dd' where deptno=20;

select * from dept where deptno=40; 

create table aa as select * from emp;
insert into aa (empno,ename,job, hiredate, sal, deptno) values(8000,'HONG', 'STUDENT', sysdate, 100,40); 
select * from aa
select empno, ename, sla

insert into aa  values(8000,¡®HONG¡¯, ¡®STUDENT¡¯, , sysdate, 100, ,40); 

 delete from aa where comm= 0 or comm is null;
 
 select distinct job from emp;
 
 insert into aa (empno, ename, job, hiredate, sal, deptno) values(8000,¡®HONG¡¯, ¡®STUDENT¡¯, sysdate, 100,40);
 
 select empno, ename, sal ,grade
 from emp, salgrage
 where emp.sal>=salgrade.losal and emp.sal<=salgrage.hisal;
 
 
    create table dd (
   ID varchar2(15) primary key,
   PASSWORD varchar2(10),
   BIRTHDAY	number(8),
   GENDER    varchar2(3) check(gender IN('³²','¿©'))
	);
	
	select deptno, sal, ename from emp e, dept d
	where (e.deptno, sal) in (select deptno, max(sal) from emp e1
	group by deptno)
	and e.deptno =d.deptno
	order by deptno asc;
	
	select mgr,name count(ename)
	from emp, emp01
	where emp.mgr =empno
	
	
	select deptno, max(sal), ename
  from emp
where e.deptno = d.deptno;
  group by deptno