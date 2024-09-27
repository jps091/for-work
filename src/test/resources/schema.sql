CREATE TABLE thumbnail_images (
                                  modified_at datetime DEFAULT NULL,
                                  registered_at datetime DEFAULT NULL,
                                  thumbnail_image_id bigint NOT NULL AUTO_INCREMENT,
                                  url varchar(255) NOT NULL,
                                  field varchar(255) NOT NULL,
                                  PRIMARY KEY (thumbnail_image_id)
);

CREATE TABLE users (
                       last_login_at bigint DEFAULT NULL,
                       modified_at datetime DEFAULT NULL,
                       registered_at datetime DEFAULT NULL,
                       user_id bigint NOT NULL AUTO_INCREMENT,
                       name varchar(10) NOT NULL,
                       email varchar(50) NOT NULL,
                       password varchar(20) NOT NULL,
                       role varchar(255) NOT NULL,
                       PRIMARY KEY (user_id),
                       UNIQUE (email)
);

CREATE TABLE resumes (
                         price decimal(6,0) NOT NULL,
                         modified_at datetime DEFAULT NULL,
                         registered_at datetime DEFAULT NULL,
                         resume_id bigint NOT NULL AUTO_INCREMENT,
                         seller_id bigint NOT NULL,
                         description_image_url varchar(255) NOT NULL,
                         resume_url varchar(255) NOT NULL,
                         description CLOB NOT NULL,
                         field varchar(255) NOT NULL,
                         level varchar(255) NOT NULL,
                         status varchar(255) NOT NULL,
                         PRIMARY KEY (resume_id),
                         FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE resume_decisions (
                                  resume_decision_id bigint NOT NULL AUTO_INCREMENT,
                                  modified_at datetime DEFAULT NULL,
                                  registered_at datetime DEFAULT NULL,
                                  status varchar(255) DEFAULT NULL,
                                  admin_id bigint DEFAULT NULL,
                                  resume_id bigint NOT NULL,
                                  PRIMARY KEY (resume_decision_id),
                                  FOREIGN KEY (admin_id) REFERENCES users(user_id),
                                  FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
);

CREATE TABLE sales_posts (
                             sales_post_id bigint NOT NULL AUTO_INCREMENT,
                             modified_at datetime DEFAULT NULL,
                             registered_at datetime DEFAULT NULL,
                             sales_quantity int NOT NULL,
                             status varchar(255) NOT NULL,
                             title varchar(255) NOT NULL,
                             view_count int NOT NULL,
                             resume_id bigint NOT NULL,
                             thumbnail_image_id bigint DEFAULT NULL,
                             PRIMARY KEY (sales_post_id),
                             UNIQUE (resume_id),
                             FOREIGN KEY (resume_id) REFERENCES resumes(resume_id),
                             FOREIGN KEY (thumbnail_image_id) REFERENCES thumbnail_images(thumbnail_image_id)
);

CREATE TABLE carts (
                       cart_id bigint NOT NULL AUTO_INCREMENT,
                       modified_at datetime DEFAULT NULL,
                       registered_at datetime DEFAULT NULL,
                       user_id bigint DEFAULT NULL,
                       status varchar(255) NOT NULL,
                       total_price decimal(8,0) NOT NULL,
                       total_quantity int NOT NULL,
                       PRIMARY KEY (cart_id),
                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE cart_resumes (
                              cart_id bigint NOT NULL,
                              cart_resume_id bigint NOT NULL AUTO_INCREMENT,
                              modified_at datetime DEFAULT NULL,
                              registered_at datetime DEFAULT NULL,
                              resume_id bigint NOT NULL,
                              PRIMARY KEY (cart_resume_id),
                              FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
                              FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
);

CREATE TABLE orders (
                        canceled_at datetime DEFAULT NULL,
                        confirmed_at datetime DEFAULT NULL,
                        modified_at datetime DEFAULT NULL,
                        ordered_at datetime DEFAULT NULL,
                        order_id bigint NOT NULL AUTO_INCREMENT,
                        registered_at datetime DEFAULT NULL,
                        user_id bigint DEFAULT NULL,
                        status varchar(255) DEFAULT NULL,
                        total_price decimal(8,0) NOT NULL,
                        PRIMARY KEY (order_id),
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE order_resumes (
                               modified_at datetime DEFAULT NULL,
                               order_id bigint NOT NULL,
                               order_resume_id bigint NOT NULL AUTO_INCREMENT,
                               registered_at datetime DEFAULT NULL,
                               resume_id bigint NOT NULL,
                               status varchar(255) NOT NULL,
                               PRIMARY KEY (order_resume_id),
                               FOREIGN KEY (order_id) REFERENCES orders(order_id),
                               FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
);

CREATE TABLE payment_entity (
                                amount decimal(38,2) NOT NULL,
                                modified_at datetime DEFAULT NULL,
                                order_id bigint NOT NULL,
                                payment_id bigint NOT NULL AUTO_INCREMENT,
                                registered_at datetime DEFAULT NULL,
                                payment_method varchar(31) NOT NULL,
                                status varchar(255) NOT NULL,
                                PRIMARY KEY (payment_id),
                                UNIQUE (order_id),
                                FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
