select * from roles;

select * from users;

select * from employees;


insert into roles values(default, 'ADMIN');
insert into roles values(default, 'MANAGER');
insert into roles values(default, 'EMPLOYEE');
insert into roles values(default, 'CUSTOMER');

insert into users values (default, 3, 'user1', '1234', 'name1', 'surname1', 'email1', 'phone1', 'ACTIVE');

insert into employees values (default, 2, '2018-08-29T06:12:15.156', 'Java developer', 'Java, Spring, Hibernate', '1.2', 
LOAD_FILE('D:\Уник\ДИПЛОМ\bigproject\images\person.jpeg'));