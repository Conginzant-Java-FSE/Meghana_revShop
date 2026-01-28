CREATE DATABASE IF NOT EXISTS revshop_db;
USE revshop_db;

CREATE TABLE buyer (
                       buyer_id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(120) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(15),
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE seller (
                        seller_id INT AUTO_INCREMENT PRIMARY KEY,
                        business_name VARCHAR(150) NOT NULL,
                        owner_name VARCHAR(100) NOT NULL,
                        email VARCHAR(120) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        phone VARCHAR(15),
                        is_active BOOLEAN DEFAULT TRUE,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        wallet_balance DOUBLE DEFAULT 0
);



CREATE TABLE security_question (
                                   question_id INT AUTO_INCREMENT PRIMARY KEY,
                                   question_text VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO security_question (question_text) VALUES
                                                  ('What is your favourite pet animal?'),
                                                  ('What is your favourite movie?'),
                                                  ('What is your birth city?'),
                                                  ('What is your nick name?');

CREATE TABLE buyer_security (
                                buyer_id INT NOT NULL,
                                question_id INT NOT NULL,
                                answer VARCHAR(255) NOT NULL,
                                PRIMARY KEY (buyer_id, question_id),
                                FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id) ON DELETE CASCADE,
                                FOREIGN KEY (question_id) REFERENCES security_question(question_id) ON DELETE CASCADE
);

CREATE TABLE seller_security (
                                 seller_id INT NOT NULL,
                                 question_id INT NOT NULL,
                                 answer VARCHAR(255) NOT NULL,
                                 PRIMARY KEY (seller_id, question_id),
                                 FOREIGN KEY (seller_id) REFERENCES seller(seller_id) ON DELETE CASCADE,
                                 FOREIGN KEY (question_id) REFERENCES security_question(question_id) ON DELETE CASCADE
);



CREATE TABLE category (
                          category_id INT AUTO_INCREMENT PRIMARY KEY,
                          category_name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO category(category_name) VALUES
                                        ('Electronics'),
                                        ('Fashion'),
                                        ('Home & Kitchen'),
                                        ('Books'),
                                        ('Beauty'),
                                        ('Sports'),
                                        ('Grocery');



CREATE TABLE product (
                         product_id INT AUTO_INCREMENT PRIMARY KEY,
                         seller_id INT NOT NULL,
                         category_id INT NOT NULL,
                         product_name VARCHAR(150) NOT NULL,
                         description TEXT,
                         mrp DECIMAL(10,2) NOT NULL,
                         discounted_price DECIMAL(10,2) NOT NULL,
                         stock_quantity INT NOT NULL,
                         stock_threshold INT DEFAULT 5,
                         is_active BOOLEAN DEFAULT TRUE,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (seller_id) REFERENCES seller(seller_id),
                         FOREIGN KEY (category_id) REFERENCES category(category_id)
);



CREATE TABLE cart (
                      cart_id INT AUTO_INCREMENT PRIMARY KEY,
                      buyer_id INT NOT NULL UNIQUE,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id)
);

CREATE TABLE cart_item (
                           cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
                           cart_id INT NOT NULL,
                           product_id INT NOT NULL,
                           quantity INT NOT NULL,
                           added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           UNIQUE(cart_id, product_id),
                           FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
                           FOREIGN KEY (product_id) REFERENCES product(product_id)
);



CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        buyer_id INT NOT NULL,
                        total_amount DECIMAL(12,2) NOT NULL,
                        payment_method VARCHAR(30),
                        shipping_address VARCHAR(255),
                        billing_address VARCHAR(255),
                        placed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id)
);

CREATE TABLE order_item (
                            order_item_id INT AUTO_INCREMENT PRIMARY KEY,
                            order_id INT NOT NULL,
                            product_id INT NOT NULL,
                            seller_id INT NOT NULL,
                            quantity INT NOT NULL,
                            unit_price DECIMAL(10,2),
                            line_total DECIMAL(12,2),
                            FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES product(product_id),
                            FOREIGN KEY (seller_id) REFERENCES seller(seller_id)
);



CREATE TABLE review (
                        review_id INT AUTO_INCREMENT PRIMARY KEY,
                        buyer_id INT NOT NULL,
                        product_id INT NOT NULL,
                        rating INT CHECK (rating BETWEEN 1 AND 5),
                        comment TEXT,
                        reviewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        UNIQUE(buyer_id, product_id),
                        FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id),
                        FOREIGN KEY (product_id) REFERENCES product(product_id)
);



CREATE TABLE favorite (
                          favorite_id INT AUTO_INCREMENT PRIMARY KEY,
                          buyer_id INT NOT NULL,
                          product_id INT NOT NULL,
                          saved_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          UNIQUE(buyer_id, product_id),
                          FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id),
                          FOREIGN KEY (product_id) REFERENCES product(product_id)
);



CREATE TABLE notification (
                              notification_id INT AUTO_INCREMENT PRIMARY KEY,
                              buyer_id INT NULL,
                              seller_id INT NULL,
                              message VARCHAR(255),
                              is_read BOOLEAN DEFAULT FALSE,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id),
                              FOREIGN KEY (seller_id) REFERENCES seller(seller_id)
);



CREATE TABLE payment (
                         payment_id INT AUTO_INCREMENT PRIMARY KEY,
                         buyer_id INT NOT NULL,
                         order_id INT NOT NULL,
                         amount DOUBLE,
                         payment_method VARCHAR(30),
                         paid_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id),
                         FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
