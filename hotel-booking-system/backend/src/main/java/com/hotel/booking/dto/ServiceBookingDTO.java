package com.hotel.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 服务预订请求 DTO（SPA/健身/泳池等酒店服务）
 */
@Data
public class ServiceBookingDTO {

    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @NotNull(message = "服务价格不能为空")
    private BigDecimal price;

    @NotNull(message = "预约日期不能为空")
    private LocalDate serviceDate;

    @NotNull(message = "人数不能为空")
    private Integer guests;

    @NotBlank(message = "客人姓名不能为空")
    private String guestName;

    @NotBlank(message = "联系电话不能为空")
    private String guestPhone;

    private String remark;

}
