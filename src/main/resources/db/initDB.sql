DROP TABLE user_roles IF EXISTS;
DROP TABLE menu_item IF EXISTS;
DROP TABLE votes IF EXISTS;
DROP TABLE dishes IF EXISTS;
DROP TABLE restaurants IF EXISTS;
DROP TABLE users IF EXISTS;

CREATE TABLE users
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    name       VARCHAR(255)            NOT NULL,
    email      VARCHAR(255)            NOT NULL,
    password   VARCHAR(255)            NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    enabled    BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    name    VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_name_idx
    ON restaurants (name);

CREATE TABLE dishes
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    price INTEGER      NOT NULL
);
CREATE UNIQUE INDEX dishes_unique_name_price_idx ON dishes (name, price);

CREATE TABLE menu_item
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    restaurant_id INTEGER NOT NULL,
    dish_id       INTEGER NOT NULL,
    date          DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menu_item_unique_idx ON menu_item (restaurant_id, dish_id, date);

CREATE TABLE votes
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    user_id       INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    date          DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX votes_unique_idx ON votes (user_id, date);