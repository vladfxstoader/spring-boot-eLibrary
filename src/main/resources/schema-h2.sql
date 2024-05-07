CREATE TABLE author_details (
  id int NOT NULL AUTO_INCREMENT,
  bio varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
--
--
CREATE TABLE author (
  id int NOT NULL AUTO_INCREMENT,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  author_details_id int DEFAULT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE author ADD FOREIGN KEY (author_details_id) REFERENCES author_details(id);
--
CREATE TABLE category (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE publisher (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--am modificat user -> users si la book year -> publishing year

CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  password varchar(255) NOT NULL,
  status varchar(255) DEFAULT NULL,
  username varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
--
CREATE TABLE role (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_role (
  user_id int NOT NULL,
  role_id int NOT NULL
);

ALTER TABLE user_role ADD FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_role ADD FOREIGN KEY (role_id) REFERENCES role(id);

CREATE TABLE past_user_role (
    user_id int NOT NULL,
    role_id int NOT NULL
);

ALTER TABLE past_user_role ADD FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE past_user_role ADD FOREIGN KEY (role_id) REFERENCES role(id);

--
CREATE TABLE book (
  id int NOT NULL AUTO_INCREMENT,
  stock int DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  publishing_year int DEFAULT NULL,
  publisher_id int DEFAULT NULL,
  PRIMARY KEY (id)
);
--
ALTER TABLE book ADD FOREIGN KEY (publisher_id) REFERENCES publisher(id);
--
CREATE TABLE book_author (
  book_id int NOT NULL,
  author_id int NOT NULL
);

ALTER TABLE book_author ADD FOREIGN KEY (book_id) REFERENCES book(id);
ALTER TABLE book_author ADD FOREIGN KEY (author_id) REFERENCES author(id);
--
CREATE TABLE book_category (
  book_id int NOT NULL,
  category_id int NOT NULL
);

ALTER TABLE book_category ADD FOREIGN KEY (book_id) REFERENCES book(id);
ALTER TABLE book_category ADD FOREIGN KEY (category_id) REFERENCES category(id);

CREATE TABLE loan (
  id int NOT NULL AUTO_INCREMENT,
  actual_return_date datetime(6) DEFAULT NULL,
  expected_return_date datetime(6) DEFAULT NULL,
  loan_date datetime(6) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  book_id int DEFAULT NULL,
  user_id int DEFAULT NULL,
  observations varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE loan ADD FOREIGN KEY (book_id) REFERENCES book(id);
ALTER TABLE loan ADD FOREIGN KEY (user_id) REFERENCES users(id);