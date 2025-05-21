INSERT INTO USERS (email, name, password)
VALUES ('user@gmail.com', 'User_First', '{noop}password'),
       ('admin@yandex.ru', 'Admin_First', '{noop}admin'),
       ('guest@gmail.com', 'Guest_First', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('Restaurant_First'),
       ('Restaurant_Second');

INSERT INTO VOTE (restaurant_id, user_id)
VALUES (1, 2),
       (2, 1);