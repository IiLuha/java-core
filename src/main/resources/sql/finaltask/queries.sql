
--Запрос для проверки джоинов
--Итоги:
--Выполнено два хэш джоина и 3 сиквенс скан по всем таблицам.
--Цена 1265к, время 50 с
--После btree индекса customer_id:
--нет изменений, хотя я ожидал merge join, так как индекс customer_id отсортирован в обеих таблицах
--полагаю - это из-за фильтра. Переделаю заполнение таблиц и начну заново
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

--Запрос для проверки джоинов в другом порядке
--То же самое, что и в предыдущем запросе. (82 с)
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

COMMIT;

--Запрос для проверки партиционирования
--Итоги:
--66 с, цена 1542к, сиквенс скан
--После btree индекса customer_id:
--нет изменений
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT * FROM orders WHERE created_at >= '2024-09-01' AND created_at
<'2025-01-31';

COMMIT;

--запрос для ознакомления с хэш-индексом
--Итоги:
--85 с, цена 1057к, сиквенс скан
--После btree индекса customer_id:
--4.5 с, цена 4к, Bitmap ind scan
--стало сильно лучше
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT * FROM orders WHERE customer_id = 32768;

COMMIT;