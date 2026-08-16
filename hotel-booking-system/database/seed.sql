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
('标准间', '舒适温馨的标准客房，配备双人床和基础设施', 299.00, 'https://picsum.photos/seed/room1/400/300', NOW(), NOW()),
('豪华间', '豪华宽敞的客房，配备高档家具和优质床品', 499.00, 'https://picsum.photos/seed/room2/400/300', NOW(), NOW()),
('商务套房', '商务人士首选，配备独立办公区和会议设施', 799.00, 'https://picsum.photos/seed/room3/400/300', NOW(), NOW()),
('海景套房', '面朝大海，春暖花开，享受极致海景体验', 999.00, 'https://picsum.photos/seed/room4/400/300', NOW(), NOW());

-- 餐厅种子数据（与 DataInitConfig 内容一致）
INSERT INTO restaurants (name, description, imageUrl, createdAt, updatedAt) VALUES
('中餐厅', '提供正宗中式菜肴，品味中华美食文化', 'https://picsum.photos/seed/restaurant1/400/300', NOW(), NOW()),
('西餐厅', '精致西餐料理，享受浪漫用餐体验', 'https://picsum.photos/seed/restaurant2/400/300', NOW(), NOW()),
('咖啡厅', '香浓咖啡，悠闲时光，商务洽谈首选', 'https://picsum.photos/seed/restaurant3/400/300', NOW(), NOW()),
('自助餐厅', '丰富多样的美食选择，满足您的味蕾', 'https://picsum.photos/seed/restaurant4/400/300', NOW(), NOW());
