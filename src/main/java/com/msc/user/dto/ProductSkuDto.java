package com.msc.user.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ProductSkuDto
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/12 13:58
 **/
@Data
public class ProductSkuDto {
    private String unit;//销售单位：个、只、包、箱、袋。。。。。等
    private List<ProductSkuDetailDto> details;//规格明细
    private Integer shelvesNum;//上架数量
    private BigDecimal skuPrice;//单价
}
