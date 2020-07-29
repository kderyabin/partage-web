INSERT INTO person(person_id, person_name) VALUES (5,'Anna'),(10,'Christopher'),(4,'James'),(6,'John'),(16,'Sarah');
INSERT INTO board(board_id, board_name, description, creation, currency, updated) VALUES (13,'Paris','','2020-07-29 06:35:11','EUR','2020-07-29 06:35:59'),(14,'Barcelona','','2020-07-29 11:33:48','USD','2020-07-29 11:39:30');
INSERT INTO board_person(board_id, person_id) VALUES (13,4),(14,4),(13,5),(14,5),(14,6),(13,10),(14,10),(13,16);
INSERT INTO item(item_id, amount, pay_date, item_title, board_id, person_id) VALUES (9,100.00,'2020-07-29','Car',13,4),(10,50.00,'2020-07-29','Lunch',13,5),(11,300.00,'2020-07-29','Boat',13,16);
INSERT INTO setting(setting_id, setting_name, setting_value) VALUES (1,'lang','en'),(2,'currency','EUR');