
--Запрос для проверки джоинов c фильтрацией по датам
--Итоги:
--Выполнено два хэш джоина и 3 сиквенс скан по всем таблицам.
--Цена 1140к-1507к, время 260 с, сам запрос выполнился за 13 секунд и
--для BETWEEN '2023-01-01' AND '2023-01-31' за 134 с
--После btree индекса на customer_id и product_id:
--нет изменений, всё ещё нет merge join
--После hash индекса на customer_id:
--нет изменений
--После партиционирования:
--Цена 221к-448к, время 52 с,
--один сиквенс скан по партициии в следствие того, что условие помещается в 1 партицию
--два сиквенс скана по кастомерс и продукстс и два хэш джоина
--значительно быстрее работает
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
--Выполнено два хэш джоина и 2 сиквенс скан по таблицам customer и product
--индекс скан по ордерс.
--Цена 402к-1571к, время 83 с
--После btree индекса на customer_id и product_id:
--Цена 402к-1571к, время 95 с
--не понимаю, почему нет  merge join и почему не стало лучше
--всё ещё сиквенс сканы
--После hash индекса на customer_id:
--нет изменений
--После партиционирования:
--Цена 402к-1224к, время 174 с,
--несколько индекс сканов по партициям (фильтрация)
--несколько сиквенс сканов по партициям (тоже фильтрация)
--два сиквенс скана по кастомерс и продукстс и два хэш джоина
--меньше производительность
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
--После btree индекса на customer_id и product_id:
--нет изменений
--После hash индекса на customer_id:
--нет изменений
--После партиционирования:
--0-1248 мс, цена 0-216к, несколько индекс сканов по партициям
--меньше производительность
--выполняется сильно быстрее
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
--После btree индекса на customer_id и product_id:
--20 мс, цена 0-28, index scan стало сильно лучше
--После hash индекса на customer_id:
--25 мс, цена 0-28, bind scan то же самое что и  при индексе btree
--После партиционирования:
--570 мс, цена 0-277, несколько индекс сканов по партициям
--меньше производительность
BEGIN TRANSACTION;

EXPLAIN ANALYZE
SELECT * FROM orders WHERE customer_id = 32768;

COMMIT;