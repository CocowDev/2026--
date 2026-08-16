-- ============================================================
-- 酒店预订管理系统 · 数据库建表脚本
-- 说明：列名采用驼峰命名，与后端实体 @TableField 注解严格一致，
--       避免 MyBatis-Plus 生成 SQL 时出现 Unknown column 错误。
--       本脚本与真实运行库（hotel_booking）结构保持一致。
-- ============================================================

CREATE DATABASE IF NOT EXISTS hotel_booking DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE hotel_booking;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '用户名（登录账号）',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密密码',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL,
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '管理员用户名',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密密码',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL,
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

-- 房型表
CREATE TABLE IF NOT EXISTS room_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL COMMENT '房型名称',
    description TEXT COMMENT '房型描述',
    price DECIMAL(10, 2) NOT NULL COMMENT '每晚单价',
    imageUrl VARCHAR(255) DEFAULT NULL COMMENT '房型图片地址',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房型表';

-- 餐厅表
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '餐厅名称',
    description TEXT COMMENT '餐厅描述',
    imageUrl VARCHAR(255) DEFAULT NULL COMMENT '餐厅图片地址',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='餐厅表';

-- 预订表（客房预订 + 餐饮预订共用）
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT DEFAULT NULL COMMENT '下单用户ID',
    roomTypeId BIGINT DEFAULT NULL COMMENT '房型ID（客房预订）',
    restaurantId BIGINT DEFAULT NULL COMMENT '餐厅ID（餐饮预订）',
    type VARCHAR(20) NOT NULL DEFAULT 'room' COMMENT '预订类型：room-客房 / restaurant-餐饮',
    checkInDate DATETIME DEFAULT NULL COMMENT '入住日期（客房）',
    checkOutDate DATETIME DEFAULT NULL COMMENT '离店日期（客房）',
    bookingDate DATETIME DEFAULT NULL COMMENT '用餐日期（餐饮）',
    bookingTime TIME DEFAULT NULL COMMENT '用餐时段（餐饮）',
    guests INT NOT NULL DEFAULT 1 COMMENT '入住/用餐人数',
    totalPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价（BigDecimal）',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending/confirmed/completed/cancelled',
    guestName VARCHAR(100) NOT NULL COMMENT '客人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱',
    specialRequests TEXT COMMENT '特殊要求',
    remark TEXT COMMENT '备注',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_userId (userId),
    KEY idx_roomTypeId (roomTypeId),
    KEY idx_type_status (type, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预订表';
