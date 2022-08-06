create sequence seq_task start with 1000;

create table task(
                     id bigint primary key,
                     name varchar(255) not null,
                     description varchar(255),
                     sec_user_id bigint not null,
                     constraint fk_task_sec_user_id
                         foreign key(sec_user_id) references sec_user(id)
);

create sequence seq_attached_file start with 1000;

create table attached_file(
                              id bigint primary key,
                              name varchar(255) not null,
                              content_type varchar(255) not null,
                              task_id bigint not null,
                              folder_path varchar(255) not null,
                              constraint fk_task_id
                                  foreign key(task_id) references task(id)
);

