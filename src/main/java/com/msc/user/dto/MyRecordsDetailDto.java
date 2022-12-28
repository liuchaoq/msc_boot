package com.msc.user.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName MyRecordsDetailDto
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/15 16:36
 **/
@Data
public class MyRecordsDetailDto {
    private Integer id;//抄表记录id:123,
    private String homeNo;//门牌号:'ceshi',
    private Integer num;//水表读数:'1456',
    private Integer count;//用水量:20,
    private BigDecimal price;//用水单价:1.5,
    private BigDecimal amount;//该门牌号使用水费:30,
    private BigDecimal payStatus;//支付状态:1,
    private String url;//水表读数图片
    private String payUser;//水费支付人姓名
    private String orderCode;//预生成水费订单编号
}
