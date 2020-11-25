ALTER ROLE $spring_datasource_username SET search_path TO seqdb,public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto" SCHEMA seqdb;
