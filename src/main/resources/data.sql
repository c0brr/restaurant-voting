INSERT INTO USERS (email, name, password)
VALUES ('user1@gmail.com', 'First_User', '{noop}password'),
       ('user2@gmail.com', 'Second_User', '{noop}password2'),
       ('user3@yandex.ru', 'Third_User', '{noop}password3'),
       ('admin@yandex.ru', 'First_Admin', '{noop}admin'),
       ('guest@gmail.com', 'First_Guest', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('USER', 4),
       ('ADMIN', 4);

INSERT INTO RESTAURANT (name)
VALUES ('First_restaurant'),
       ('Second_restaurant'),
       ('Third_restaurant'),
       ('Fourth_restaurant');

INSERT INTO VOTE (restaurant_id, user_id)
VALUES (1, 2),
       (2, 1),
       (1, 3),
       (1, 4);

INSERT INTO MENU (restaurant_id)
VALUES (1),
       (2);

INSERT INTO DISH (name, price, menu_id)
VALUES ('Soup', 100, 1),
       ('Salad', 200, 1);