CREATE TABLE thumbnail_images (
                                  modified_at timestamp DEFAULT NULL,
                                  registered_at timestamp DEFAULT NULL,
                                  thumbnail_image_id bigint NOT NULL AUTO_INCREMENT,
                                  url varchar(255) NOT NULL,
                                  field varchar(255) NOT NULL,
                                  PRIMARY KEY (thumbnail_image_id)
);

CREATE TABLE users (
                       last_login_at timestamp DEFAULT NULL,
                       modified_at timestamp DEFAULT NULL,
                       registered_at timestamp DEFAULT NULL,
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
                         modified_at timestamp DEFAULT NULL,
                         registered_at timestamp DEFAULT NULL,
                         resume_id bigint NOT NULL AUTO_INCREMENT,
                         seller_id bigint NOT NULL,
                         sales_quantity int NOT NULL,
                         description_image_url varchar(300) NOT NULL,
                         resume_url varchar(300) NOT NULL,
                         description varchar(5000) NOT NULL,
                         field varchar(255) NOT NULL,
                         level varchar(255) NOT NULL,
                         status varchar(255) NOT NULL,
                         PRIMARY KEY (resume_id),
                         FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE resume_decisions (
                                  resume_decision_id bigint NOT NULL AUTO_INCREMENT,
                                  modified_at timestamp DEFAULT NULL,
                                  registered_at timestamp DEFAULT NULL,
                                  status varchar(255) DEFAULT NULL,
                                  admin_id bigint DEFAULT NULL,
                                  resume_id bigint NOT NULL,
                                  PRIMARY KEY (resume_decision_id),
                                  FOREIGN KEY (admin_id) REFERENCES users(user_id),
                                  FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
);

CREATE TABLE sales_posts (
                             sales_post_id bigint NOT NULL AUTO_INCREMENT,
                             modified_at timestamp DEFAULT NULL,
                             registered_at timestamp DEFAULT NULL,
                             status varchar(255) NOT NULL,
                             title varchar(25) NOT NULL,
                             resume_id bigint NOT NULL,
                             thumbnail_image_id bigint DEFAULT NULL,
                             PRIMARY KEY (sales_post_id),
                             UNIQUE (resume_id),
                             FOREIGN KEY (resume_id) REFERENCES resumes(resume_id),
                             FOREIGN KEY (thumbnail_image_id) REFERENCES thumbnail_images(thumbnail_image_id)
);

CREATE TABLE carts (
                       cart_id bigint NOT NULL AUTO_INCREMENT,
                       modified_at timestamp DEFAULT NULL,
                       registered_at timestamp DEFAULT NULL,
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
                              modified_at timestamp DEFAULT NULL,
                              registered_at timestamp DEFAULT NULL,
                              resume_id bigint NOT NULL,
                              PRIMARY KEY (cart_resume_id),
                              FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
                              FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
);

CREATE TABLE orders (
                        modified_at timestamp DEFAULT NULL,
                        order_id bigint NOT NULL AUTO_INCREMENT,  -- Use AUTO_INCREMENT for H2
                        registered_at timestamp DEFAULT NULL,
                        user_id bigint DEFAULT NULL,
                        request_id varchar(25) NOT NULL,
                        paid_at timestamp DEFAULT NULL,
                        total_amount decimal(8, 0) NOT NULL,
                        status varchar(20) NOT NULL,  -- Replacing ENUM with VARCHAR(20) for H2
                        PRIMARY KEY (order_id),
                        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL -- Foreign key constraint
);

CREATE TABLE order_resumes (
                               modified_at timestamp DEFAULT NULL,
                               order_id bigint NOT NULL,
                               order_resume_id bigint NOT NULL AUTO_INCREMENT,
                               registered_at timestamp DEFAULT NULL,
                               resume_id bigint NOT NULL,
                               status varchar(255) NOT NULL,
                               confirmed_at timestamp DEFAULT NULL,
                               sent_at timestamp DEFAULT NULL,
                               canceled_at timestamp DEFAULT NULL,
                               PRIMARY KEY (order_resume_id),
                               FOREIGN KEY (order_id) REFERENCES orders(order_id),
                               FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
);

CREATE TABLE transactions (
                              amount decimal(7, 0) NOT NULL,
                              modified_at timestamp DEFAULT NULL,
                              registered_at timestamp DEFAULT NULL,
                              transaction_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              payment_key varchar(25) NOT NULL,
                              request_id varchar(25) NOT NULL,
                              user_email varchar(50) NOT NULL,
                              type varchar(255) NOT NULL
);

CREATE TABLE mail_logs (
                           mail_log_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           modified_at timestamp DEFAULT NULL,
                           registered_at timestamp DEFAULT NULL,
                           resume_id bigint NOT NULL,
                           request_id varchar(25) NOT NULL,
                           email varchar(50) NOT NULL,
                           error_response varchar(255) DEFAULT NULL,
                           result varchar(255) NOT NULL
);
