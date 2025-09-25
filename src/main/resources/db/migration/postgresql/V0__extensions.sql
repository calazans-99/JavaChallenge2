-- V0__extensions.sql (POSTGRES)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- V0__extensions.sql (POSTGRES) - patched para Azure Flexible Server
-- Substitui uuid-ossp por pgcrypto (permitida) e cria um shim para uuid_generate_v4()

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$
BEGIN
  IF to_regprocedure('uuid_generate_v4()') IS NULL THEN
    CREATE OR REPLACE FUNCTION uuid_generate_v4() RETURNS uuid
    LANGUAGE sql AS $$ SELECT gen_random_uuid() $$;
  END IF;
END $$;
