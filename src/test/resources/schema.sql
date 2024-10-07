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

CREATE TABLE retry_logs (
                            retry_log_id bigint NOT NULL AUTO_INCREMENT,  -- H2에서는 AUTO_INCREMENT 사용
                            modified_at timestamp DEFAULT NULL,
                            registered_at timestamp DEFAULT NULL,
                            error_response varchar(255) NOT NULL,
                            request_id varchar(255) NOT NULL,
                            type varchar(20) NOT NULL,  -- ENUM 대신 VARCHAR(20) 사용
                            PRIMARY KEY (retry_log_id)
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
                        confirmed_at timestamp DEFAULT NULL,
                        modified_at timestamp DEFAULT NULL,
                        ordered_at timestamp DEFAULT NULL,
                        order_id bigint NOT NULL AUTO_INCREMENT,  -- Use AUTO_INCREMENT for H2
                        registered_at timestamp DEFAULT NULL,
                        user_id bigint DEFAULT NULL,
                        request_id varchar(255) NOT NULL,
                        paid_at timestamp DEFAULT NULL,
                        total_amount decimal(8, 0) NOT NULL,
                        status varchar(20) NOT NULL,  -- Replacing ENUM with VARCHAR(20) for H2
                        PRIMARY KEY (order_id),
                        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL -- Foreign key constraint
);

CREATE TABLE order_resumes (
                               modified_at datetime DEFAULT NULL,
                               order_id bigint NOT NULL,
                               order_resume_id bigint NOT NULL AUTO_INCREMENT,
                               registered_at datetime DEFAULT NULL,
                               resume_id bigint NOT NULL,
                               status varchar(255) NOT NULL,
                               sent_at timestamp DEFAULT NULL,
                               canceled_at timestamp DEFAULT NULL,
                               PRIMARY KEY (order_resume_id),
                               FOREIGN KEY (order_id) REFERENCES orders(order_id),
                               FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
);

CREATE TABLE transactions (
                              transaction_id bigint NOT NULL AUTO_INCREMENT,  -- Use AUTO_INCREMENT for H2
                              modified_at timestamp DEFAULT NULL,
                              registered_at timestamp DEFAULT NULL,
                              amount decimal(7,0) NOT NULL,
                              charged_at timestamp DEFAULT NULL,
                              order_id bigint NOT NULL,
                              type varchar(20) NOT NULL,  -- Replace ENUM with VARCHAR(20)
                              user_id bigint NOT NULL,
                              payment_key varchar(255) NOT NULL,
                              PRIMARY KEY (transaction_id),
                              FOREIGN KEY (order_id) REFERENCES orders (order_id), -- Foreign key for order_id
                              FOREIGN KEY (user_id) REFERENCES users (user_id)    -- Foreign key for user_id
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
