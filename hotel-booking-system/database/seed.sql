-- ============================================================
-- 酒店预订管理系统 · 种子数据脚本
-- 说明：
--   1. admin 账号不再由本脚本写入，统一由后端 DataInitConfig 初始化（admin/admin123），
--      避免 seed.sql 与 DataInitConfig 双轨密码不一致（历史缺陷 P1-4 已修复）。
--   2. 列名采用驼峰命名，与 schema.sql / 后端实体保持一致。
-- ============================================================

USE hotel_booking;

-- 房型种子数据（与 DataInitConfig 内容一致；表非空时由后端守卫跳过）
INSERT INTO room_types (title, description, price, imageUrl, createdAt, updatedAt) VALUES
('标准间', '舒适温馨的标准客房，配备双人床和基础设施', 299.00, '/images/room-single.jpg', NOW(), NOW()),
('豪华间', '豪华宽敞的客房，配备高档家具和优质床品', 499.00, '/images/room-double.jpg', NOW(), NOW()),
('商务套房', '商务人士首选，配备独立办公区和会议设施', 799.00, '/images/room-business.jpg', NOW(), NOW()),
('海景套房', '面朝大海，春暖花开，享受极致海景体验', 999.00, '/images/room-ocean.jpg', NOW(), NOW());

-- 餐厅种子数据（与 DataInitConfig 内容一致）
INSERT INTO restaurants (name, description, imageUrl, createdAt, updatedAt) VALUES
('中餐厅', '提供正宗中式菜肴，品味中华美食文化', '/images/restaurant-chinese.jpg', NOW(), NOW()),
('西餐厅', '精致西餐料理，享受浪漫用餐体验', '/images/restaurant-luxury.jpg', NOW(), NOW()),
('咖啡厅', '香浓咖啡，悠闲时光，商务洽谈首选', '/images/restaurant-cafe.jpg', NOW(), NOW()),
('自助餐厅', '丰富多样的美食选择，满足您的味蕾', '/images/restaurant-buffet.jpg', NOW(), NOW());

-- 餐厅菜品种子数据（餐饮预订第二步点选）
INSERT INTO dishes (restaurantId, name, description, price, imageUrl, createdAt, updatedAt) VALUES
(1, '红烧狮子头', '经典淮扬名菜，肉质鲜嫩', 68.00, '/images/dish-01.jpg', NOW(), NOW()),
(1, '宫保鸡丁', '川味经典，微辣鲜香', 48.00, '/images/dish-02.jpg', NOW(), NOW()),
(1, '清蒸鲈鱼', '鲜嫩多汁，原汁原味', 88.00, '/images/dish-03.jpg', NOW(), NOW()),
(2, '安格斯牛排', '精选安格斯牛眼肉，七分熟最佳', 188.00, '/images/dish-04.jpg', NOW(), NOW()),
(2, '法式焗蜗牛', '经典法式前菜，蒜香浓郁', 98.00, '/images/dish-05.jpg', NOW(), NOW()),
(2, '提拉米苏', '意式经典甜点，入口即化', 58.00, '/images/dish-06.jpg', NOW(), NOW()),
(3, '美式拿铁', '香醇浓缩咖啡搭配绵密奶泡', 32.00, '/images/dish-07.jpg', NOW(), NOW()),
(3, '蓝山手冲', '精选蓝山咖啡豆，手冲萃取', 48.00, '/images/dish-08.jpg', NOW(), NOW()),
(3, '芝士蛋糕', '纽约风味，浓郁芝士', 38.00, '/images/dish-09.jpg', NOW(), NOW()),
(4, '海鲜自助拼盘', '当日鲜捕海鲜，无限畅享', 268.00, '/images/dish-10.jpg', NOW(), NOW()),
(4, '日式刺身拼盘', '三文鱼/金枪鱼/甜虾，新鲜直供', 158.00, '/images/dish-11.jpg', NOW(), NOW()),
(4, '现烤羊排', '外焦里嫩，香料入味', 128.00, '/images/dish-12.jpg', NOW(), NOW());

