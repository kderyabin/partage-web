create table board ( board_id integer auto_increment, board_name varchar(50) not null, description varchar(255), creation timestamp not null,currency varchar(3),`updated` timestamp not null, primary key (board_id));
create table person ( person_id integer auto_increment,  person_name varchar(50) not null,  primary key (person_id));
create table board_person (  board_id integer not null,  person_id integer not null,  primary key (board_id, person_id));
create table item ( item_id integer auto_increment,  amount decimal(19,2) not null,  pay_date date not null,  item_title varchar(50) not null,  board_id integer not null,  person_id integer not null,  primary key (item_id));
create table setting ( setting_id integer auto_increment,  setting_name varchar(30) not null,  setting_value varchar(255),  primary key (setting_id),  constraint unique_name unique (setting_name));
alter table board_person  add constraint fk_partcipants_person   foreign key (person_id)   references person (person_id)   on delete cascade;
alter table board_person  add constraint fk_partcipants_board   foreign key (board_id)   references board (board_id)   on delete cascade;
alter table item   add constraint fk_items_board   foreign key (board_id)   references board (board_id)   on delete cascade;
alter table item   add constraint fk_items_person   foreign key (person_id)   references person (person_id)   on delete cascade;
