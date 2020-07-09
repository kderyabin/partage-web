SET FOREIGN_KEY_CHECKS=0;

drop table if exists board;
create table board (
   board_id integer auto_increment,
   	board_name varchar(50) not null,
	description varchar(255),
	creation timestamp not null,
	currency varchar(3),
	`update` timestamp not null,
	primary key (board_id)
);

drop table if exists person;
create table person (
   person_id integer auto_increment,
	person_name varchar(50) not null,
	primary key (person_id)
);

drop table if exists board_person;
create table board_person (
  board_id integer not null,
	person_id integer not null,
	primary key (board_id, person_id)
);

 drop table if exists item;
create table item (
   item_id integer auto_increment,
	amount decimal(19,2) not null,
	pay_date date not null,
	item_title varchar(50) not null,
	board_id integer not null,
	person_id integer not null,
	primary key (item_id)
);

 drop table if exists setting;
create table setting (
   setting_id integer auto_increment,
	setting_name varchar(30) not null,
	setting_value varchar(255),
	primary key (setting_id),
	constraint unique_name unique (setting_name)
);

drop table if exists `user`;
create table `user` (
	user_id varchar(36) not null,
    name varchar(100) not null,
	login varchar(100) not null,
	pwd varchar(255) not null,
	confirmed boolean,
	token varchar(255),
	primary key(user_id),
	constraint unique_login  unique (login),
	constraint unique_token  unique (token)
);

drop table if exists mail;
 create table mail (
    mail_id integer auto_increment,
    `action` varchar(10) not null,
    creation timestamp not null,
    token varchar(36) not null,
    user_id varchar(255),
	primary key (mail_id)
);


-- Foreign keys
alter table board_person 
   add constraint fk_partcipants_person 
   foreign key (person_id) 
   references person (person_id)
   on delete cascade;

alter table board_person 
   add constraint fk_partcipants_board 
   foreign key (board_id) 
   references board (board_id)
   on delete cascade;


alter table item 
   add constraint fk_items_board
   foreign key (board_id) 
   references board (board_id)
   on delete cascade;


alter table item 
   add constraint fk_items_person 
   foreign key (person_id) 
   references person (person_id)
   on delete cascade;

 alter table mail
    add constraint fk_user_id
    foreign key (user_id)
    references user(user_id)
    on delete cascade;

SET FOREIGN_KEY_CHECKS=0;