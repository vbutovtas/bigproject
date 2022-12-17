
insert into roles values(default, 'ROLE_ADMIN');
insert into roles values(default, 'ROLE_MANAGER');
insert into roles values(default, 'ROLE_EMPLOYEE');
insert into roles values(default, 'ROLE_CUSTOMER');

insert into users values (default, 1, 'user1', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Liza', 'Yakovleva', 'marusika6464@mail.ru', '+12345', 'ACTIVE');
insert into users values (default, 2, 'user2', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Alexandra', 'Lappo', 'marusika6464@mail.ru', '+12345', 'ACTIVE');
insert into users values (default, 3, 'user3', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Lera', 'Tripuz', 'marusika6464@mail.ru', '+12345', 'ACTIVE');
insert into users values (default, 4, 'user4', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Vitaliy', 'Butovtas', 'marusika6464@mail.ru', '+12345', 'ACTIVE');
insert into users values (default, 5, 'user5', '$2a$10$r22vvkcw0qIeChQSlKRObOCjZjTuEVb20DXE8oAgc7dUE7WePEiaC', 'Ivan', 'Ivanov', 'marusika6464@mail.ru', '+12345', 'ACTIVE');

insert into employees values (default, 1, '2002-07-23', 'Middle Java developer', 'Java, Spring, Hibernate', '2023-01-03', '1.3', null);
insert into employees values (default, 2, '1980-10-01', 'QA', 'Selenium, JUnit', '2022-12-17', '1.0', null);
insert into employees values (default, 3, '1980-10-01', 'Javacript Dev', 'HTML, CSS, JS, React Native', '2022-12-17', '1.0', null);
insert into employees values (default, 4, '1997-12-21', 'Middle Java developer | Dev Lead', 'Java, Spring, Hibernate, Jenkins', '2022-12-17', '2.6', null);
insert into employees values (default, 5, '2002-07-23', 'C# developer', 'WPF, ASP.Net, APS.Core', '2022-12-17', '0.5', null);

/* 
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
*/


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

/* CALL reorderTickets(5, 1, 'IN_BUILD', 1, 'OPEN'); */

