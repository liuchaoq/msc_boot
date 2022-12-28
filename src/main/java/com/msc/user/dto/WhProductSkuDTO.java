package com.msc.user.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName WhProductSkuDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/27 17:02
 **/
@Data
public class WhProductSkuDTO {
    private Integer id;
    private BigDecimal skuPrice;
    private Integer sellNum;
    private Integer tradeNum;
    private String unit;
    private List<WhProductSkuDetailDTO> details;
}
