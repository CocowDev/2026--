package com.hotel.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {

    @NotNull(message = "房型ID不能为空")
    private Long roomTypeId;

    @NotBlank(message = "客人姓名不能为空")
    private String guestName;

    @NotBlank(message = "联系电话不能为空")
    private String guestPhone;

    @NotBlank(message = "电子邮箱不能为空")
    private String guestEmail;

    // 入住日期：前端按 yyyy-MM-dd 提交，LocalDate 与实体/DB 类型一致
    @NotNull(message = "入住日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    // 离店日期：同上
    @NotNull(message = "离店日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @NotNull(message = "入住人数不能为空")
    private Integer guestCount;

    private String specialRequests;

}
