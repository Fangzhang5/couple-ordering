-- 小程序点单系统初始化脚本
-- MySQL 8.x
-- 执行方式：
-- mysql -uroot -p < deploy/sql/init.sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS couple_ordering
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE couple_ordering;

DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户主键',
    openid VARCHAR(64) NOT NULL COMMENT '微信小程序用户唯一标识',
    nickname VARCHAR(64) NULL COMMENT '用户昵称',
    avatar_url VARCHAR(512) NULL COMMENT '用户头像地址',
    role TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色：0普通用户，1管理员',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    last_login_time DATETIME NULL COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_openid (openid),
    CONSTRAINT chk_user_role CHECK (role IN (0, 1)),
    CONSTRAINT chk_user_status CHECK (status IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户表';

CREATE TABLE category (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类主键',
    name VARCHAR(32) NOT NULL COMMENT '分类名称',
    sort INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_name (name),
    KEY idx_category_status_sort (status, sort),
    CONSTRAINT chk_category_status CHECK (status IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '菜品分类表';

CREATE TABLE dish (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜品主键',
    category_id BIGINT UNSIGNED NOT NULL COMMENT '所属分类ID',
    name VARCHAR(64) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '菜品价格，单位元',
    image_url VARCHAR(512) NULL COMMENT '菜品图片地址',
    description VARCHAR(255) NULL COMMENT '菜品描述',
    status TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0下架，1上架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_dish_category_status (category_id, status),
    KEY idx_dish_name (name),
    CONSTRAINT fk_dish_category
        FOREIGN KEY (category_id) REFERENCES category (id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT chk_dish_price CHECK (price >= 0),
    CONSTRAINT chk_dish_status CHECK (status IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '菜品表';

CREATE TABLE shopping_cart (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车项主键',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    dish_id BIGINT UNSIGNED NOT NULL COMMENT '菜品ID',
    quantity INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_user_dish (user_id, dish_id),
    KEY idx_cart_user (user_id),
    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id) REFERENCES user (id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT fk_cart_dish
        FOREIGN KEY (dish_id) REFERENCES dish (id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT chk_cart_quantity CHECK (quantity > 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '购物车表';

CREATE TABLE orders (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单主键',
    order_number VARCHAR(32) NOT NULL COMMENT '订单号',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '下单用户ID',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1
        COMMENT '订单状态：1待接单，2已接单，3制作中，4已完成，5已取消',
    amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    remark VARCHAR(255) NULL COMMENT '用户备注',
    expected_time DATETIME NULL COMMENT '期望用餐时间',
    cancel_reason VARCHAR(255) NULL COMMENT '取消原因',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_number (order_number),
    KEY idx_orders_user_time (user_id, create_time),
    KEY idx_orders_status_time (status, create_time),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES user (id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT chk_orders_status CHECK (status IN (1, 2, 3, 4, 5)),
    CONSTRAINT chk_orders_amount CHECK (amount >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '订单表';

CREATE TABLE order_item (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单明细主键',
    order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    dish_id BIGINT UNSIGNED NULL COMMENT '下单时菜品ID，不设置菜品外键',
    dish_name VARCHAR(64) NOT NULL COMMENT '下单时菜品名称快照',
    dish_image_url VARCHAR(512) NULL COMMENT '下单时菜品图片快照',
    dish_price DECIMAL(10, 2) NOT NULL COMMENT '下单时菜品单价',
    quantity INT UNSIGNED NOT NULL COMMENT '购买数量',
    amount DECIMAL(10, 2) NOT NULL COMMENT '明细金额',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_order_item_order (order_id),
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT chk_order_item_price CHECK (dish_price >= 0),
    CONSTRAINT chk_order_item_quantity CHECK (quantity > 0),
    CONSTRAINT chk_order_item_amount CHECK (amount >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '订单明细表';

-- 第一阶段演示数据
INSERT INTO category (id, name, sort, status)
VALUES
    (1, '家常菜', 1, 1),
    (2, '主食', 2, 1),
    (3, '饮品', 3, 1);

INSERT INTO dish (
    id, category_id, name, price, image_url, description, status
)
VALUES
    (1, 1, '番茄炒蛋', 12.00, NULL, '酸甜家常口味', 1),
    (2, 1, '可乐鸡翅', 28.00, NULL, '甜咸口味', 1),
    (3, 1, '青椒土豆丝', 10.00, NULL, '清爽微辣', 1),
    (4, 2, '米饭', 2.00, NULL, '一份米饭', 1),
    (5, 2, '蛋炒饭', 15.00, NULL, '鸡蛋与米饭炒制', 1),
    (6, 3, '可乐', 5.00, NULL, '冰镇饮品', 1),
    (7, 3, '柠檬水', 6.00, NULL, '清爽柠檬味', 0);

SET FOREIGN_KEY_CHECKS = 1;

-- 验证初始化结果
SELECT id, name, sort, status
FROM category
ORDER BY sort, id;

SELECT
    d.id,
    d.category_id,
    c.name AS category_name,
    d.name,
    d.price,
    d.status
FROM dish d
JOIN category c ON c.id = d.category_id
ORDER BY d.id;
