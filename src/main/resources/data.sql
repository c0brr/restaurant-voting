INSERT INTO USERS (EMAIL, NAME, PASSWORD)
VALUES ('user@gmail.com', 'First_User', '{noop}password'),
       ('user2@gmail.com', 'Second_User', '{noop}password2'),
       ('user3@yandex.ru', 'Third_User', '{noop}password3'),
       ('admin@yandex.ru', 'First_Admin', '{noop}admin'),
       ('guest@gmail.com', 'First_Guest', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('USER', 4),
       ('ADMIN', 4);

INSERT INTO RESTAURANT (NAME, CREATED)
VALUES ('First_restaurant', '2024-01-01'),
       ('Second_restaurant', '2025-01-01'),
       ('Third_restaurant', '2025-06-06'),
       ('Fourth_restaurant', '2024-06-06');

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, CREATION_DATE)
VALUES (1, 2, '2025-07-07'),
       (1, 2, '2025-07-28'),
       (1, 1, '2025-07-29'),
       (1, 3, '2025-07-30'),
       (2, 1, '2025-07-07'),
       (3, 1, '2025-07-07');

INSERT INTO MENU (RESTAURANT_ID, CREATION_DATE)
VALUES (1, '2024-05-06'),
       (1, '2024-06-06'),
       (1, '2024-06-07'),
       (1, '2024-06-08'),
       (1, '2025-01-01'),
       (2, '2024-01-01');

INSERT INTO DISH (NAME, PRICE, MENU_ID)
VALUES ('Soup', 260, 1),
       ('Salad', 200, 1),
       ('Fries', 100, 1),
       ('Apple juice', 150, 1),
       ('Salad', 50, 2);