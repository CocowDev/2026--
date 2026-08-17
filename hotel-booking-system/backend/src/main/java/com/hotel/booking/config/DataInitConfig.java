package com.hotel.booking.config;

import com.hotel.booking.entity.Admin;
import com.hotel.booking.entity.Dish;
import com.hotel.booking.entity.Restaurant;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.AdminMapper;
import com.hotel.booking.mapper.DishMapper;
import com.hotel.booking.mapper.RestaurantMapper;
import com.hotel.booking.mapper.RoomTypeMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initData(AdminMapper adminMapper, RoomTypeMapper roomTypeMapper, 
                                      RestaurantMapper restaurantMapper, DishMapper dishMapper,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminMapper.selectCount(null) == 0) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                adminMapper.insert(admin);
                System.out.println("管理员账号已创建：admin / admin123");
            }
            
            if (roomTypeMapper.selectCount(null) == 0) {
                List<RoomType> roomTypes = new ArrayList<>();
                
                RoomType r1 = new RoomType();
                r1.setTitle("标准间");
                r1.setDescription("舒适温馨的标准客房，配备双人床和基础设施");
                r1.setPrice(new BigDecimal("299.00"));
                r1.setImageUrl("/images/room-single.jpg");
                r1.setCreatedAt(LocalDateTime.now());
                r1.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r1);
                
                RoomType r2 = new RoomType();
                r2.setTitle("豪华间");
                r2.setDescription("豪华宽敞的客房，配备高档家具和优质床品");
                r2.setPrice(new BigDecimal("499.00"));
                r2.setImageUrl("/images/room-double.jpg");
                r2.setCreatedAt(LocalDateTime.now());
                r2.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r2);
                
                RoomType r3 = new RoomType();
                r3.setTitle("商务套房");
                r3.setDescription("商务人士首选，配备独立办公区和会议设施");
                r3.setPrice(new BigDecimal("799.00"));
                r3.setImageUrl("/images/room-business.jpg");
                r3.setCreatedAt(LocalDateTime.now());
                r3.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r3);
                
                RoomType r4 = new RoomType();
                r4.setTitle("海景套房");
                r4.setDescription("面朝大海，春暖花开，享受极致海景体验");
                r4.setPrice(new BigDecimal("999.00"));
                r4.setImageUrl("/images/room-ocean.jpg");
                r4.setCreatedAt(LocalDateTime.now());
                r4.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r4);
                
                roomTypes.forEach(roomTypeMapper::insert);
                System.out.println("房型数据已初始化");
            }
            
            if (restaurantMapper.selectCount(null) == 0) {
                List<Restaurant> restaurants = new ArrayList<>();
                
                Restaurant re1 = new Restaurant();
                re1.setName("中餐厅");
                re1.setDescription("提供正宗中式菜肴，品味中华美食文化");
                re1.setImageUrl("/images/restaurant-chinese.jpg");
                re1.setCreatedAt(LocalDateTime.now());
                re1.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re1);
                
                Restaurant re2 = new Restaurant();
                re2.setName("西餐厅");
                re2.setDescription("精致西餐料理，享受浪漫用餐体验");
                re2.setImageUrl("/images/restaurant-luxury.jpg");
                re2.setCreatedAt(LocalDateTime.now());
                re2.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re2);
                
                Restaurant re3 = new Restaurant();
                re3.setName("咖啡厅");
                re3.setDescription("香浓咖啡，悠闲时光，商务洽谈首选");
                re3.setImageUrl("/images/restaurant-cafe.jpg");
                re3.setCreatedAt(LocalDateTime.now());
                re3.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re3);
                
                Restaurant re4 = new Restaurant();
                re4.setName("自助餐厅");
                re4.setDescription("丰富多样的美食选择，满足您的味蕾");
                re4.setImageUrl("/images/restaurant-buffet.jpg");
                re4.setCreatedAt(LocalDateTime.now());
                re4.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re4);
                
                restaurants.forEach(restaurantMapper::insert);
                System.out.println("餐厅数据已初始化");
            }
            
            if (dishMapper.selectCount(null) == 0) {
                List<Dish> dishes = new ArrayList<>();
                // 中餐厅（restaurantId=1）
                dishes.add(buildDish(1L, "红烧狮子头", "经典淮扬名菜，肉质鲜嫩", "68.00", 1));
                dishes.add(buildDish(1L, "宫保鸡丁", "川味经典，微辣鲜香", "48.00", 2));
                dishes.add(buildDish(1L, "清蒸鲈鱼", "鲜嫩多汁，原汁原味", "88.00", 3));
                // 西餐厅（restaurantId=2）
                dishes.add(buildDish(2L, "安格斯牛排", "精选安格斯牛眼肉，七分熟最佳", "188.00", 4));
                dishes.add(buildDish(2L, "法式焗蜗牛", "经典法式前菜，蒜香浓郁", "98.00", 5));
                dishes.add(buildDish(2L, "提拉米苏", "意式经典甜点，入口即化", "58.00", 6));
                // 咖啡厅（restaurantId=3）
                dishes.add(buildDish(3L, "美式拿铁", "香醇浓缩咖啡搭配绵密奶泡", "32.00", 7));
                dishes.add(buildDish(3L, "蓝山手冲", "精选蓝山咖啡豆，手冲萃取", "48.00", 8));
                dishes.add(buildDish(3L, "芝士蛋糕", "纽约风味，浓郁芝士", "38.00", 9));
                // 自助餐厅（restaurantId=4）
                dishes.add(buildDish(4L, "海鲜自助拼盘", "当日鲜捕海鲜，无限畅享", "268.00", 10));
                dishes.add(buildDish(4L, "日式刺身拼盘", "三文鱼/金枪鱼/甜虾，新鲜直供", "158.00", 11));
                dishes.add(buildDish(4L, "现烤羊排", "外焦里嫩，香料入味", "128.00", 12));
                dishes.forEach(dishMapper::insert);
                System.out.println("菜品种子数据已初始化");
            }
            
            System.out.println("种子数据初始化完成");
        };
    }

    /** 构造菜品（含时间戳） */
    private Dish buildDish(Long restaurantId, String name, String description, String price, int dishNo) {
        Dish dish = new Dish();
        dish.setRestaurantId(restaurantId);
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(new BigDecimal(price));
        dish.setImageUrl("/images/dish-" + String.format("%02d", dishNo) + ".jpg");
        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        return dish;
    }

}
