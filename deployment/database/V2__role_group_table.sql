create sequence seq_sec_role_group start with 1000;

create table sec_role_group(
                               id bigint primary key,
                               role_group_name varchar(255) not null unique
);

create table sec_role_group_sec_role(
                                        sec_role_group_id bigint,
                                        sec_role_id bigint,
                                        constraint fk_sec_role_group_id
                                            foreign key(sec_role_group_id) references sec_role_group(id),
                                        constraint fk_sec_role_id_role_group
                                            foreign key(sec_role_id) references sec_role(id)
);


create table sec_user_sec_role_group(
                                        sec_user_id bigint,
                                        sec_role_group_id bigint,
                                        constraint fk_user_id_role_group
                                            foreign key(sec_user_id) references sec_user(id),
                                        constraint fk_sec_role_group_id_sec_user
                                            foreign key(sec_role_group_id) references sec_role_group(id)
);
