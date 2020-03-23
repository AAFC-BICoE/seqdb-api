SELECT count(usename) from pg_catalog.pg_user where usename in ('$POSTGRES_USER', '$spring_datasource_username', '$spring_liquibase_user');
