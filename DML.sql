select * from roles;

select * from users;

select * from employees;

select * from tickets order by tickets.status, tickets.order_number;

select * from projects_has_employees;


insert into roles values(default, 'ADMIN');
insert into roles values(default, 'MANAGER');
insert into roles values(default, 'EMPLOYEE');
insert into roles values(default, 'CUSTOMER');

insert into users values (default, 3, 'user1', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'name1', 'surname1', 'email1', 'phone1', 'ACTIVE');
insert into users values (default, 3, 'user2', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'name2', 'surname2', 'email2', 'phone2', 'ACTIVE');
insert into users values (default, 2, 'manager1', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'name3', 'surname3', 'email3', 'phone3', 'ACTIVE');
insert into users values (default, 3, 'qwert', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Vitaliy', 'Butovtas', 'qwerrt@mail.ru', '+643234', 'ACTIVE');
insert into users values (default, 3, 'liza', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Liza', 'Ivanova', 'liza@gmail.com', '+34563231', 'ACTIVE');

insert into employees values (default, 1, '1998-05-16', 'Middle Java developer', 'Java, Spring, Hibernate', '2018-11-29', '1.2', null);
insert into employees values (default, 2, '1980-10-01', 'Senior Java developer', 'Java, Spring, Hibernate, Docker', '2015-01-17', '3.5', null);
insert into employees values (default, 3, '1980-10-01', 'Senior Java developer', 'Java, Spring, Hibernate, Docker', '2015-01-17', '3.5', null);
insert into employees values (default, 4, '1997-12-21', 'Middle Java developer | Dev Lead', 'Java, Spring, Hibernate, Jenkins', '2015-01-17', '2.3', null);
insert into employees values (default, 5, '2002-07-23', 'Junior Java developer', 'Java, Spring, Hibernate', '2015-01-17', '0.5', null);

insert into tickets values (default, 1, 1, null, 'project1', 'description1', NOW(),  '2024-01-01', 0, 0, 'OPEN', 'PROJECT', 
2, 'https://github.com/vbutovtas/bigproject', 1);
insert into tickets values (default, 2, 2, 1, 'ticket1', 'ticket1description1', NOW(), '2024-01-01', 0, 0, 'OPEN', 'TASK', 
1, 'https://github.com/vbutovtas/bigproject', 2);
insert into tickets values (default, 2, 2, 1, 'ticket2', 'ticket1description2', NOW(), '2024-01-01', 0, 0, 'OPEN', 'TASK', 
2, 'https://github.com/vbutovtas/bigproject', 3);
insert into tickets values (default, 2, 2, 1, 'ticket3', 'ticket1description3', NOW(), '2024-01-01', 0, 0, 'IN_BUILD', 'TASK', 
3, 'https://github.com/vbutovtas/bigproject', 4);
insert into tickets values (default, 2, 2, 1, 'ticket4', 'ticket1description3', NOW(), '2024-01-01', 0, 0, 'IN_BUILD', 'TASK', 
4, 'https://github.com/vbutovtas/bigproject', 5);
insert into tickets values (default, 2, 2, 1, 'ticket5', 'ticket1description3', NOW(), '2024-01-01', 0, 0, 'IN_BUILD', 'TASK', 
2, 'https://github.com/vbutovtas/bigproject', 6);
insert into tickets values (default, 2, 2, 1, 'ticket6', 'ticket1description3', NOW(), '2022-10-31 15:12:56', 0, 0, 'IN_BUILD', 'TASK', 
2, 'https://github.com/vbutovtas/bigproject', 6);

insert into projects_has_employees values (1, 1);


DROP PROCEDURE IF EXISTS reorderTickets;
delimiter $$
CREATE PROCEDURE reorderTickets(IN ticket_id INT, IN startOrder INT, IN startColumn VARCHAR(30), IN finishOrder INT, IN finishColumn VARCHAR(30))
BEGIN
	DECLARE id int;
	DECLARE order_value varchar(30);
    DECLARE cursor_finished int DEFAULT 0;
	DECLARE curStartColumn 
		CURSOR FOR 
			SELECT tickets.id, tickets.order_number FROM tickets 
				where tickets.status = startColumn AND tickets.order_number > startOrder;
	DECLARE curFinishColumn 
		CURSOR FOR 
			SELECT tickets.id, tickets.order_number FROM tickets 
				where tickets.status = finishColumn AND tickets.order_number >= finishOrder;	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET cursor_finished = 1;
	
	OPEN curStartColumn;
    startColumn_loop: LOOP
		FETCH curStartColumn INTO id, order_value;
		IF cursor_finished = 1 THEN LEAVE startColumn_loop;
		END IF;
        update tickets set tickets.order_number = order_value - 1 where tickets.id = id;
    END LOOP startColumn_loop;
    CLOSE curStartColumn;
    SET cursor_finished = 0;
    
	OPEN curFinishColumn;
    finishColumn_loop: LOOP
		FETCH curFinishColumn INTO id, order_value;
		IF cursor_finished = 1 THEN LEAVE finishColumn_loop;
		END IF;
        update tickets set tickets.order_number = order_value + 1 where tickets.id = id;
    END LOOP finishColumn_loop;
    CLOSE curFinishColumn; 
    
    update tickets set tickets.order_number = finishOrder, tickets.status = finishColumn where tickets.id = ticket_id;
    commit;
END$$
delimiter ;


CALL reorderTickets(5, 1, 'IN_BUILD', 1, 'OPEN');

