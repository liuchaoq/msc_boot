package com.msc.user.dto;

import com.msc.user.entity.TOrderProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName OrderInfoDTO
 * @DESCRIPTION 订单基础信息表
 * @AUTHOR liuCq
 * @DATE 2022/7/21 14:12
 **/
@Data
public class OrderInfoDTO {
    private String code;
    private BigDecimal amount;
    private List<TOrderProduct> productList;
    //支付状态0未支付，1已支付
    private Integer payStatus;
    //如果已支付，支付人姓名
    private String payUserName;

}
