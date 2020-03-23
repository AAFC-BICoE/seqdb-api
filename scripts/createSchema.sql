CREATE SCHEMA $spring_liquibase_defaultSchema;

GRANT USAGE ON SCHEMA $spring_liquibase_defaultSchema TO $spring_datasource_username;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA $spring_liquibase_defaultSchema TO $spring_datasource_username;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA $spring_liquibase_defaultSchema TO $spring_datasource_username;

alter default privileges in schema $spring_liquibase_defaultSchema grant SELECT, INSERT, UPDATE, DELETE on tables to $spring_datasource_username;
alter default privileges in schema $spring_liquibase_defaultSchema grant all on sequences to $spring_datasource_username;
