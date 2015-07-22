INSERT INTO app_user (id, username, password, first_name, last_name, city, country, email, phone_number, postal_code, province, website, password_hint, version, account_enabled, account_expired, account_locked, credentials_expired) VALUES (-1, 'user', '$2a$10$CnQVJ9bsWBjMpeSKrrdDEeuIptZxXrwtI6CZ/OgtNxhIgpKxXeT9y', 'Tomcat', 'User', 'Denver', 'US', 'matt_raible@yahoo.com', '', '80210', 'CO', 'http://tomcat.apache.org', 'A male kitty,', 1, 1, 0, 0, 0);
INSERT INTO app_user (id, username, password, first_name, last_name, city, country, email, phone_number, postal_code, province, website, password_hint, version, account_enabled, account_expired, account_locked, credentials_expired) VALUES (-2, 'admin', '$2a$10$bH/ssqW8OhkTlIso9/yakubYODUOmh.6m5HEJvcBq3t3VdBh7ebqO', 'Matt', 'Raible', 'Denver', 'US', 'matt@raibledesign.com', '', '80210', 'CO', 'http://raibledesigns.com', 'Not a female kitty,', 1, 1, 0, 0, 0);
INSERT INTO app_user (id, username, password, first_name, last_name, city, country, email, phone_number, postal_code, province, website, password_hint, version, account_enabled, account_expired, account_locked, credentials_expired) VALUES (-3, 'two_roles_user', '$2a$10$bH/ssqW8OhkTlIso9/yakubYODUOmh.6m5HEJvcBq3t3VdBh7ebqO', 'Two Roles', 'User', 'Denver', 'US', 'two_roles_user@appfuse.org', '', '80210', 'CO', 'http://raibledesign.com', 'Not a female kitty,', 1, 1, 0, 0, 0);

INSERT INTO role (id, name, description) VALUES (-1, 'ROLE_ADMIN', 'Administrator role (can edit Users)');
INSERT INTO role (id, name, description) VALUES (-2, 'ROLE_USER', 'Default role for all Users');

INSERT INTO user_role (user_id, role_id) VALUES (-1, -2);
INSERT INTO user_role (user_id, role_id) VALUES (-2, -1);
INSERT INTO user_role (user_id, role_id) VALUES (-3, -1);
INSERT INTO user_role (user_id, role_id) VALUES (-3, -2);
