package com.hotel.booking.config;

import com.hotel.booking.entity.Admin;
import com.hotel.booking.entity.Restaurant;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.AdminMapper;
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
                                      RestaurantMapper restaurantMapper, PasswordEncoder passwordEncoder) {
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
                r1.setImageUrl("https://picsum.photos/seed/room1/400/300");
                r1.setCreatedAt(LocalDateTime.now());
                r1.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r1);
                
                RoomType r2 = new RoomType();
                r2.setTitle("豪华间");
                r2.setDescription("豪华宽敞的客房，配备高档家具和优质床品");
                r2.setPrice(new BigDecimal("499.00"));
                r2.setImageUrl("https://picsum.photos/seed/room2/400/300");
                r2.setCreatedAt(LocalDateTime.now());
                r2.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r2);
                
                RoomType r3 = new RoomType();
                r3.setTitle("商务套房");
                r3.setDescription("商务人士首选，配备独立办公区和会议设施");
                r3.setPrice(new BigDecimal("799.00"));
                r3.setImageUrl("https://picsum.photos/seed/room3/400/300");
                r3.setCreatedAt(LocalDateTime.now());
                r3.setUpdatedAt(LocalDateTime.now());
                roomTypes.add(r3);
                
                RoomType r4 = new RoomType();
                r4.setTitle("海景套房");
                r4.setDescription("面朝大海，春暖花开，享受极致海景体验");
                r4.setPrice(new BigDecimal("999.00"));
                r4.setImageUrl("https://picsum.photos/seed/room4/400/300");
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
                re1.setImageUrl("https://picsum.photos/seed/restaurant1/400/300");
                re1.setCreatedAt(LocalDateTime.now());
                re1.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re1);
                
                Restaurant re2 = new Restaurant();
                re2.setName("西餐厅");
                re2.setDescription("精致西餐料理，享受浪漫用餐体验");
                re2.setImageUrl("https://picsum.photos/seed/restaurant2/400/300");
                re2.setCreatedAt(LocalDateTime.now());
                re2.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re2);
                
                Restaurant re3 = new Restaurant();
                re3.setName("咖啡厅");
                re3.setDescription("香浓咖啡，悠闲时光，商务洽谈首选");
                re3.setImageUrl("https://picsum.photos/seed/restaurant3/400/300");
                re3.setCreatedAt(LocalDateTime.now());
                re3.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re3);
                
                Restaurant re4 = new Restaurant();
                re4.setName("自助餐厅");
                re4.setDescription("丰富多样的美食选择，满足您的味蕾");
                re4.setImageUrl("https://picsum.photos/seed/restaurant4/400/300");
                re4.setCreatedAt(LocalDateTime.now());
                re4.setUpdatedAt(LocalDateTime.now());
                restaurants.add(re4);
                
                restaurants.forEach(restaurantMapper::insert);
                System.out.println("餐厅数据已初始化");
            }
            
            System.out.println("种子数据初始化完成");
        };
    }

}
