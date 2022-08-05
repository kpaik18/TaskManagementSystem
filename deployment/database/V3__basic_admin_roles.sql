insert into sec_role(id, name)
values
(1, 'sec_user_create'),
(2, 'sec_user_read'),
(3, 'sec_user_update'),
(4, 'sec_user_delete');

insert into sec_user_sec_role(sec_user_id, sec_role_id)
values
(1, 1),
(1, 2),
(1, 3),
(1, 4);