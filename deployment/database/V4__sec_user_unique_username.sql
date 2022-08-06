alter table sec_user
    add constraint uk_sec_user_username unique(username);