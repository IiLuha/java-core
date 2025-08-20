BEGIN TRANSACTION;

DROP TABLE orders;
DROP TABLE customers;
DROP TABLE products;

COMMIT;

BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS orders (
order_id SERIAL PRIMARY KEY,
customer_id INT,
product_id INT,
created_at TIMESTAMP NOT NULL,
amount NUMERIC(10,2),
status TEXT
);

CREATE INDEX idx_btree_orders_customer_id
ON orders(customer_id);
CREATE INDEX idx_btree_orders_product_id
ON orders(product_id);

CREATE TABLE IF NOT EXISTS customers (
customer_id SERIAL PRIMARY KEY,
name TEXT,
email TEXT
);

CREATE TABLE IF NOT EXISTS products (
product_id SERIAL PRIMARY KEY,
name TEXT,
price NUMERIC(10,2)
);

COMMIT;

BEGIN TRANSACTION;

CREATE OR REPLACE FUNCTION orders_add_amount() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
UPDATE public.orders o
	SET  amount=(SELECT price FROM products p WHERE new.product_id = p.product_id)
	WHERE new.order_id = o.order_id;
RETURN NULL;
END
$$;

CREATE OR REPLACE TRIGGER orders_add_amount_trigger
AFTER INSERT
ON orders
FOR ROW
EXECUTE FUNCTION orders_add_amount();

COMMIT;
