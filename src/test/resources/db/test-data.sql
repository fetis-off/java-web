INSERT INTO category (id, name, description)
VALUES (1, 'Food', 'Food products'),
       (2, 'Electronics', 'Electronic devices'),
       (3, 'Books', 'Books and magazines');

INSERT INTO product (id, name, description, price, category_id)
VALUES (1, 'Banana', 'Banana is a yellow fruit', 15.99, 1),
       (2, 'Apple', 'Red apple fruit', 12.50, 1),
       (3, 'Orange', 'Fresh orange', 18.00, 1),
       (4, 'Laptop', 'High-performance laptop', 1299.99, 2),
       (5, 'Mouse', 'Wireless mouse', 29.99, 2),
       (6, 'Java Book', 'Learn Java programming', 45.00, 3),
       (7, 'Spring Guide', 'Spring Framework guide', 55.00, 3),
       (8, 'Bread', 'Fresh bread', 3.50, 1),
       (9, 'Milk', 'Fresh milk', 2.99, 1),
       (10, 'Cheese', 'Cheddar cheese', 8.99, 1),
       (11, 'Keyboard', 'Mechanical keyboard', 89.99, 2);

SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));
SELECT setval('product_id_seq', (SELECT MAX(id) FROM product));