drop table if exists `mail`;
drop table if exists `user`;
create table `user` (
    user_id varchar(36) not null,
    name varchar(50) not null,
    login varchar(100) not null,
    pwd varchar(255) not null,
    confirmed boolean default false,
    token_api varchar(255),
    primary key (user_id),
	key login_pwd_idx(login, pwd)
) engine=innodb default charset=utf8mb4 ;

create table `mail` (
    mail_id integer not null auto_increment,
    action varchar(10) not null,
    creation timestamp not null,
    token varchar(36) not null,
    user_id varchar(36),
    primary key (mail_id),
	key token_idx(`token`),
	key `fk_user_id` (`user_id`),
    constraint `fk_user_id` foreign key (`user_id`) references `user` (`user_id`) on delete cascade
) engine=innodb default charset=utf8mb4 ;