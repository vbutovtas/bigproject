select * from roles;

select * from users;

select * from employees;

select * from tickets;

select * from projects_has_employees;


insert into roles values(default, 'ADMIN');
insert into roles values(default, 'MANAGER');
insert into roles values(default, 'EMPLOYEE');
insert into roles values(default, 'CUSTOMER');

insert into users values (default, 3, 'user1', '1234', 'name1', 'surname1', 'email1', 'phone1', 'ACTIVE');
insert into users values (default, 3, 'user2', '1234', 'name2', 'surname2', 'email2', 'phone2', 'ACTIVE');

insert into employees values (default, 1, '1998-05-16', 'Middle Java developer', 'Java, Spring, Hibernate', '2018-11-29', '1.2', null);
insert into employees values (default, 2, '1980-10-01', 'Senior Java developer', 'Java, Spring, Hibernate, Docker', '2015-01-17', '3.5', null);

insert into tickets values (default, 1, 1, null, 'project1', 'description1',  '2024-01-01', 0, 0, 'OPEN', 'PROJECT', 
2, 'https://github.com/vbutovtas/bigproject');

insert into projects_has_employees values (1, 1);