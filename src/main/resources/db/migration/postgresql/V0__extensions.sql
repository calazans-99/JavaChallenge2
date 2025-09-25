-- Sem EXTENSIONS; funções puras de UUID v4
CREATE OR REPLACE FUNCTION gen_random_uuid() RETURNS uuid
LANGUAGE sql VOLATILE AS $$
  SELECT (
    lpad(to_hex(floor(random()*4294967295)::bigint),8,'0') || '-' ||
    lpad(to_hex(floor(random()*65535)::bigint),4,'0')      || '-' ||
    lpad(to_hex(((floor(random()*4095)::bigint) | x'4000')::int),4,'0') || '-' ||
    lpad(to_hex(((floor(random()*16383)::bigint) | x'8000')::int),4,'0') || '-' ||
    lpad(to_hex(floor(random()*4294967295)::bigint),8,'0') ||
    lpad(to_hex(floor(random()*65535)::bigint),4,'0')
  )::uuid;
$$;

CREATE OR REPLACE FUNCTION uuid_generate_v4() RETURNS uuid
LANGUAGE sql STABLE AS $$
  SELECT gen_random_uuid();
$$;
