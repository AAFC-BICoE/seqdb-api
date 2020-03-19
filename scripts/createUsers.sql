CREATE USER $spring.datasource.username NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN PASSWORD '$spring.datasource.password';
GRANT CONNECT ON DATABASE $POSTGRES_DB TO $spring.datasource.username;

CREATE USER $spring.liquibase.user NOSUPERUSER NOCREATEROLE INHERIT LOGIN PASSWORD '$spring.liquibase.password';
GRANT CONNECT, CREATE ON DATABASE $POSTGRES_DB TO $spring.liquibase.user;
GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $spring.liquibase.user;
