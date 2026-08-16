package com.hotel.booking.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预订列表/详情联表展示 VO
 *
 * 承接 BookingMapper 联表查询（LEFT JOIN users / room_types）返回的别名字段，
 * application.yml 已开启 map-underscore-to-camel-case，
 * 因此 SQL 别名 user_name/user_phone/room_title 等会自动映射到本 VO 的驼峰属性。
 * 解决了历史缺陷：原 JOIN 字段无实体承载导致预订列表缺失用户名/房型名。
 */
@Data
public class BookingVO {

    // —— 预订基础字段（与 bookings 表列一致）——
    private Long id;
    private Long userId;
    private Long roomTypeId;
    private Long restaurantId;

    // 预订类型：room-客房 / restaurant-餐饮
    private String type;

    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestCount;
    private String specialRequests;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // —— 联表展示字段（JOIN 别名映射）——
    /** 下单用户姓名（users.username） */
    private String userName;

    /** 下单用户手机号（users.phone） */
    private String userPhone;

    /** 房型名称（room_types.title） */
    private String roomTitle;

    /** 房型单价（room_types.price） */
    private BigDecimal roomPrice;

    /** 房型图片（room_types.imageUrl） */
    private String roomImageUrl;

    /** 房型描述（room_types.description） */
    private String roomDescription;

}
