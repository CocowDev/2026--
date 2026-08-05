USE hotel_booking;

INSERT INTO admins (username, password, created_at, updated_at) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', NOW(), NOW());

INSERT INTO room_types (title, description, price, image_url, created_at, updated_at) VALUES
('标准间', '舒适温馨的标准客房，配备双人床和基础设施', 299.00, 'https://picsum.photos/seed/room1/400/300', NOW(), NOW()),
('豪华间', '豪华宽敞的客房，配备高档家具和优质床品', 499.00, 'https://picsum.photos/seed/room2/400/300', NOW(), NOW()),
('商务套房', '商务人士首选，配备独立办公区和会议设施', 799.00, 'https://picsum.photos/seed/room3/400/300', NOW(), NOW()),
('海景套房', '面朝大海，春暖花开，享受极致海景体验', 999.00, 'https://picsum.photos/seed/room4/400/300', NOW(), NOW());

INSERT INTO restaurants (name, description, image_url, created_at, updated_at) VALUES
('中餐厅', '提供正宗中式菜肴，品味中华美食文化', 'https://picsum.photos/seed/restaurant1/400/300', NOW(), NOW()),
('西餐厅', '精致西餐料理，享受浪漫用餐体验', 'https://picsum.photos/seed/restaurant2/400/300', NOW(), NOW()),
('咖啡厅', '香浓咖啡，悠闲时光，商务洽谈首选', 'https://picsum.photos/seed/restaurant3/400/300', NOW(), NOW()),
('自助餐厅', '丰富多样的美食选择，满足您的味蕾', 'https://picsum.photos/seed/restaurant4/400/300', NOW(), NOW());