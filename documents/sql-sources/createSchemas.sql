CREATE SCHEMA university_schemas;

GRANT SELECT ON ALL SEQUENCES IN SCHEMA university_schemas TO university;
GRANT SELECT ON ALL TABLES IN SCHEMA university_schemas TO university;
ALTER DEFAULT PRIVILEGES IN SCHEMA university_schemas GRANT SELECT ON TABLES TO university;