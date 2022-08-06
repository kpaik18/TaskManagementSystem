alter table sec_role_group_sec_role
    add constraint uk_sec_role_group_sec_role
        unique(sec_role_group_id, sec_role_id);

alter table sec_user_sec_role
    add constraint uk_sec_user_sec_role
        unique(sec_user_id, sec_role_id);