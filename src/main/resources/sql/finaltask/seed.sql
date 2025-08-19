--BEGIN TRANSACTION;
--
--CREATE SEQUENCE customer_id_seq_gendata
--    INCREMENT BY 1
--    MINVALUE 1
--    MAXVALUE 50000000
--    START WITH 1
--    CACHE 20
--    NO CYCLE;
--CREATE SEQUENCE product_id_seq_gendata
--    INCREMENT BY 1
--    MINVALUE 1
--    MAXVALUE 50000000
--    START WITH 1
--    CACHE 20
--    NO CYCLE;
--
--COMMIT;

BEGIN TRANSACTION;

INSERT INTO products (name, price)
SELECT
  'Customer ' || gs                                                             AS name,
  CAST(round( CAST(random() * 900 + 100 AS NUMERIC) , 2) AS NUMERIC(10,2))     AS price
FROM  generate_series(1, 50000) gs;

COMMIT;

BEGIN TRANSACTION;

INSERT INTO orders (customer_id, product_id, created_at, status)
SELECT
--  (SELECT nextval('customer_id_seq_gendata'))           AS customer_id,
--  (SELECT nextval('product_id_seq_gendata'))            AS product_id,
  CAST(round(random() * 49999 + 1) AS INT)            AS customer_id,
  CAST(round(random() * 49999 + 1) AS INT)            AS product_id,
  gs::date                                            AS created_at,
  CASE
    WHEN gs < '2025-06-01'::date THEN 'CLOSED'
    ELSE 'IN_PROGRESS'
  END                                                 AS status
FROM generate_series('2023-01-01'::date,
           '2025-09-27'::date,
           '1 day') gs
CROSS JOIN generate_series(1, 50000) filler;

COMMIT;

BEGIN TRANSACTION;

INSERT INTO customers (name, email)
SELECT
  'Customer ' || gs  AS name,
  'email ' || gs     AS email
FROM  generate_series(1, 50000) gs;

COMMIT;