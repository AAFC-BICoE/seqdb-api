CREATE SCHEMA $spring.liquibase.defaultSchema;

GRANT USAGE ON SCHEMA $spring.liquibase.defaultSchema TO $spring.datasource.username;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA $spring.liquibase.defaultSchema TO $spring.datasource.username;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA $spring.liquibase.defaultSchema TO $spring.datasource.username;

alter default privileges in schema $spring.liquibase.defaultSchema grant SELECT, INSERT, UPDATE, DELETE on tables to $spring.datasource.username;
alter default privileges in schema $spring.liquibase.defaultSchema grant all on sequences to $spring.datasource.username;
