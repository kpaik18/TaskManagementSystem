insert into sec_role(id, name)
values
(9, 'task_create'),
(10, 'task_read'),
(11, 'task_update'),
(12, 'task_delete');

insert into sec_user_sec_role(sec_user_id, sec_role_id)
values
(1, 9),
(1, 10),
(1, 11),
(1, 12);