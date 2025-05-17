INSERT INTO USERS (EMAIL, NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'password'),
       ('user2@gmail.com', 'Second_user', 'password2'),
       ('admin@yandex.ru', 'Admin_First', 'admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 3),
       ('USER', 3),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('Restaurant1'),
       ('Restaurant2');

INSERT INTO VOTE (RESTAURANT_ID, USER_ID)
VALUES (1, 2),
       (2, 1);