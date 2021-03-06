drop table if exists mail;
drop table if exists user;
create table user (
    user_id varchar(36) not null,
    name varchar(50) not null,
    login varchar(100) not null,
    pwd varchar(255),
    confirmed boolean default false,
    token_api varchar(255),
    primary key (user_id)
);
create index login_pwd_idx on user(login, pwd);

create table mail (
    mail_id long auto_increment,
    action varchar(10) not null,
    creation timestamp not null,
    token varchar(36) not null,
    user_id varchar(36),
    primary key (mail_id),
    constraint `fk_user_id` foreign key (`user_id`) references `user` (`user_id`) on delete cascade
);
create index token_idx on mail(token);