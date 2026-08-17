-- ============================================================
-- 真实图片接线脚本：将占位图（picsum.photos）替换为本地真实图片
-- 前置条件：frontend/public/images/ 下已按 README.md 清单放置 24 张图片
-- 执行后全站图片切换为本地真实图片（离线可用）
-- ============================================================

USE hotel_booking;

-- 房型图片
UPDATE room_types SET imageUrl = '/images/room-single.jpg'   WHERE title = '豪华单人间';
UPDATE room_types SET imageUrl = '/images/room-double.jpg'   WHERE title = '豪华双人间';
UPDATE room_types SET imageUrl = '/images/room-business.jpg' WHERE title = '商务套房';
UPDATE room_types SET imageUrl = '/images/room-family.jpg'   WHERE title = '家庭套房';
UPDATE room_types SET imageUrl = '/images/room-executive.jpg' WHERE title = '行政套房';
UPDATE room_types SET imageUrl = '/images/room-ocean.jpg'    WHERE title = '海景房';

-- 餐厅图片
UPDATE restaurants SET imageUrl = '/images/restaurant-luxury.jpg'  WHERE name = '豪华餐厅';
UPDATE restaurants SET imageUrl = '/images/restaurant-seafood.jpg'  WHERE name = '海鲜餐厅';
UPDATE restaurants SET imageUrl = '/images/restaurant-chinese.jpg'  WHERE name = '中餐厅';
UPDATE restaurants SET imageUrl = '/images/restaurant-buffet.jpg'   WHERE name = '自助餐厅';
UPDATE restaurants SET imageUrl = '/images/restaurant-bar.jpg'      WHERE name = '酒吧';
UPDATE restaurants SET imageUrl = '/images/restaurant-cafe.jpg'     WHERE name = '咖啡厅';

-- 菜品图片（按菜品 ID）
UPDATE dishes SET imageUrl = '/images/dish-01.jpg' WHERE id = 1;
UPDATE dishes SET imageUrl = '/images/dish-02.jpg' WHERE id = 2;
UPDATE dishes SET imageUrl = '/images/dish-03.jpg' WHERE id = 3;
UPDATE dishes SET imageUrl = '/images/dish-04.jpg' WHERE id = 4;
UPDATE dishes SET imageUrl = '/images/dish-05.jpg' WHERE id = 5;
UPDATE dishes SET imageUrl = '/images/dish-06.jpg' WHERE id = 6;
UPDATE dishes SET imageUrl = '/images/dish-07.jpg' WHERE id = 7;
UPDATE dishes SET imageUrl = '/images/dish-08.jpg' WHERE id = 8;
UPDATE dishes SET imageUrl = '/images/dish-09.jpg' WHERE id = 9;
UPDATE dishes SET imageUrl = '/images/dish-10.jpg' WHERE id = 10;
UPDATE dishes SET imageUrl = '/images/dish-11.jpg' WHERE id = 11;
UPDATE dishes SET imageUrl = '/images/dish-12.jpg' WHERE id = 12;

-- 校验：应输出 6/6/12 行
SELECT 'room_types updated', COUNT(*) FROM room_types WHERE imageUrl LIKE '/images/%'
UNION ALL
SELECT 'restaurants updated', COUNT(*) FROM restaurants WHERE imageUrl LIKE '/images/%'
UNION ALL
SELECT 'dishes updated', COUNT(*) FROM dishes WHERE imageUrl LIKE '/images/%';
