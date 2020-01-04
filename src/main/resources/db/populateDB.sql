DELETE
FROM user_roles;
DELETE
FROM menu_item;
DELETE
FROM votes;
DELETE
FROM dishes;
DELETE
FROM restaurants;
DELETE
FROM users;

INSERT INTO users (id, name, email, password)
VALUES (1, 'Admin', 'admin@gmail.com', '{noop}adminPassword'),
       (2, 'User', 'user@yandex.ru', '{noop}userPassword'),
       (3, 'User2', 'user2@yandex.ru', '{noop}user2Password');

INSERT INTO restaurants (id, name)
VALUES (1, 'Mirazur Restaurant'),
       (2, 'Noma Restaurant'),
       (3, 'Asador Restaurant');

INSERT INTO dishes (id, name, price)
VALUES (1, 'Ranch Crusted Chicken', 1056),
       (2, 'House Salad', 500),
       (3, 'Crispy Fish Tacos', 780),
       (4, 'Cheese Burger', 655),
       (5, 'Sundried Tomato Chicken Kabobs', 998),
       (6, 'Balsamic Grilled Chicken', 1300),
       (7, 'Fresh Salmon', 760);

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_ADMIN', 1),
       ('ROLE_USER', 2),
       ('ROLE_USER', 3);

INSERT INTO menu_item (id, restaurant_id, date, dish_id)
VALUES (1, 1, '2019-11-01', 4),
       (2, 1, '2019-11-01', 5),
       (3, 1, '2019-11-01', 6),
       (4, 1, current_date, 1),
       (5, 1, current_date, 2),
       (6, 1, current_date, 3),
       (7, 2, current_date, 3),
       (8, 2, current_date, 4),
       (9, 2, current_date, 5),
       (10, 2, current_date, 6),
       (11, 2, current_date, 7);

INSERT INTO votes (id, user_id, date, restaurant_id)
VALUES (1, 2, '2019-11-01', 1),
       (2, 2, current_date, 2);