-- ============================================================
-- 存量数据库迁移脚本：餐饮菜品功能（dishes / booking_dishes 表）
-- 新装库无需执行（schema.sql 已包含）；存量库执行本脚本即可
-- ============================================================

USE hotel_booking;

-- 餐厅菜品表
CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurantId BIGINT NOT NULL COMMENT '所属餐厅ID',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    description VARCHAR(255) COMMENT '菜品描述',
    price DECIMAL(10, 2) NOT NULL COMMENT '菜品单价',
    imageUrl VARCHAR(255) DEFAULT NULL COMMENT '菜品图片',
    createdAt DATETIME DEFAULT NULL,
    updatedAt DATETIME DEFAULT NULL,
    KEY idx_restaurantId (restaurantId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='餐厅菜品表';

-- 预订-菜品关联表
CREATE TABLE IF NOT EXISTS booking_dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bookingId BIGINT NOT NULL COMMENT '预订ID',
    dishId BIGINT NOT NULL COMMENT '菜品ID',
    dishName VARCHAR(100) NOT NULL COMMENT '菜品名称（冗余存储便于展示）',
    price DECIMAL(10, 2) NOT NULL COMMENT '菜品单价（冗余存储）',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    KEY idx_bookingId (bookingId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预订-菜品关联表';

-- 菜品种子数据（表为空时插入）
INSERT INTO dishes (restaurantId, name, description, price, imageUrl, createdAt, updatedAt)
SELECT * FROM (
  SELECT 1, '红烧狮子头', '经典淮扬名菜，肉质鲜嫩', 68.00, '/images/dish-01.jpg', NOW(), NOW() UNION ALL
  SELECT 1, '宫保鸡丁', '川味经典，微辣鲜香', 48.00, '/images/dish-02.jpg', NOW(), NOW() UNION ALL
  SELECT 1, '清蒸鲈鱼', '鲜嫩多汁，原汁原味', 88.00, '/images/dish-03.jpg', NOW(), NOW() UNION ALL
  SELECT 2, '安格斯牛排', '精选安格斯牛眼肉，七分熟最佳', 188.00, '/images/dish-04.jpg', NOW(), NOW() UNION ALL
  SELECT 2, '法式焗蜗牛', '经典法式前菜，蒜香浓郁', 98.00, '/images/dish-05.jpg', NOW(), NOW() UNION ALL
  SELECT 2, '提拉米苏', '意式经典甜点，入口即化', 58.00, '/images/dish-06.jpg', NOW(), NOW() UNION ALL
  SELECT 3, '美式拿铁', '香醇浓缩咖啡搭配绵密奶泡', 32.00, '/images/dish-07.jpg', NOW(), NOW() UNION ALL
  SELECT 3, '蓝山手冲', '精选蓝山咖啡豆，手冲萃取', 48.00, '/images/dish-08.jpg', NOW(), NOW() UNION ALL
  SELECT 3, '芝士蛋糕', '纽约风味，浓郁芝士', 38.00, '/images/dish-09.jpg', NOW(), NOW() UNION ALL
  SELECT 4, '海鲜自助拼盘', '当日鲜捕海鲜，无限畅享', 268.00, '/images/dish-10.jpg', NOW(), NOW() UNION ALL
  SELECT 4, '日式刺身拼盘', '三文鱼/金枪鱼/甜虾，新鲜直供', 158.00, '/images/dish-11.jpg', NOW(), NOW() UNION ALL
  SELECT 4, '现烤羊排', '外焦里嫩，香料入味', 128.00, '/images/dish-12.jpg', NOW(), NOW()
) t
WHERE NOT EXISTS (SELECT 1 FROM dishes);
