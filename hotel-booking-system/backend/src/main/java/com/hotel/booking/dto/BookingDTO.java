package com.hotel.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    @NotBlank(message = "入住日期不能为空")
    private String checkInDate;

    @NotBlank(message = "离店日期不能为空")
    private String checkOutDate;

    @NotNull(message = "入住人数不能为空")
    private Integer guestCount;

    private String specialRequests;

}
