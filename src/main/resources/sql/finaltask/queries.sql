
--Запрос для проверки джоинов c фильтрацией по датам
--Итоги:
--Выполнено два хэш джоина и 3 сиквенс скан по всем таблицам.
--Цена 1140к-1507к, время 260 с, сам запрос выполнился за 13 секунд и
--для BETWEEN '2023-01-01' AND '2023-01-31' за 134 с
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT o.order_id,
       u.name,
       p.name,
       o.amount,
       p.price,
       o.status
FROM orders AS o
JOIN customers AS u ON o.customer_id = u.customer_id
JOIN products AS p ON o.product_id = p.product_id
WHERE o.created_at BETWEEN '2025-01-01' AND '2025-01-31';

COMMIT;

--Запрос для проверки джоинов c фильтрацией по индексу
--Итоги:
--Выполнено два хэш джоина и 2 сиквенс скан по  таблицам customer и product
--индекс скан по ордерс.
--Цена 402к-1571к, время 83 с
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT o.order_id,
       u.name,
       p.name,
       o.amount,
       p.price,
       o.status
FROM orders AS o
JOIN customers AS u ON o.customer_id = u.customer_id
JOIN products AS p ON o.product_id = p.product_id
WHERE o.order_id > 32768 AND o.order_id < 12000000;

COMMIT;

--Запрос для проверки партиционирования
--Итоги:
--259 с, цена 1544к, сиквенс скан
--запустил analyze ещё раз: 54 с, цена 1544к, сиквенс скан
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT * FROM orders WHERE created_at >= '2024-09-01' AND created_at
<'2025-01-31';

COMMIT;

--запрос для ознакомления с хэш-индексом
--"тест равенства в where"
--Итоги:
--255 с, цена 1057к, сиквенс скан
--запустил analyze ещё раз: 3 с, цена 1к-1057к, сиквенс скан
--После btree индекса customer_id:
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT * FROM orders WHERE customer_id = 32768;

COMMIT;