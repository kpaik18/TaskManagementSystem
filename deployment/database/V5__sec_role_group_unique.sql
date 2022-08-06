alter table sec_role_group
    add constraint uk_sec_role_group_name
        unique(role_group_name);