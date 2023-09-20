CREATE USER deal_admin NOSUPERUSER NOCREATEDB NOCREATEROLE PASSWORD 'deal_admin_p';
CREATE USER deal_user NOSUPERUSER NOCREATEDB NOCREATEROLE PASSWORD 'deal_user_p';

CREATE DATABASE deal OWNER deal_admin ENCODING 'UTF8' CONNECTION LIMIT 100;
\c deal;

GRANT CONNECT ON DATABASE deal TO deal_user;

CREATE SCHEMA deal AUTHORIZATION deal_admin;
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE ON ALL TABLES IN SCHEMA deal TO deal_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA deal TO deal_user;
GRANT USAGE ON SCHEMA deal TO deal_user;

CREATE SCHEMA deal_liquibase AUTHORIZATION deal_admin;
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE ON ALL TABLES IN SCHEMA deal_liquibase TO deal_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA deal_liquibase TO deal_user;
GRANT USAGE ON SCHEMA deal_liquibase TO deal_user;

ALTER DATABASE deal set search_path TO 'deal';