create sequence seq_sec_user
    start with 1000;

create table sec_user(
                         id bigint primary key,
                         username varchar(255) not null,
                         password varchar(300) not null
);

insert into sec_user(id, username, password)
values
(1,
 'admin',
 '$2a$12$fUKIkR4R9l57FhzNESwciuaKPYo8hIWxd48xENvHofITgbvlE0.H6');

create sequence seq_sec_role start with 1000;

create table sec_role(
                         id bigint primary key,
                         name varchar(255) not null
);

create table sec_user_sec_role(
                                  sec_user_id bigint,
                                  sec_role_id bigint,
                                  constraint fk_sec_user_id
                                      foreign key(sec_user_id) references sec_user(id),
                                  constraint fk_sec_role_id
                                      foreign key(sec_role_id) references sec_role(id)
);

