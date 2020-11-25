CREATE SCHEMA IF NOT EXISTS seqdb;
REVOKE CREATE ON SCHEMA seqdb FROM PUBLIC;

GRANT USAGE ON SCHEMA seqdb TO $spring_datasource_username;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA seqdb TO $spring_datasource_username;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA seqdb TO $spring_datasource_username;

alter default privileges in schema seqdb grant SELECT, INSERT, UPDATE, DELETE, REFERENCES on tables to $spring_datasource_username;
alter default privileges in schema seqdb grant SELECT, USAGE ON sequences to $spring_datasource_username;

CREATE EXTENSION IF NOT EXISTS "pgcrypto" SCHEMA seqdb;
