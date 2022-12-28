package com.msc.user.dto;

import lombok.Data;

/**
 * @ClassName ProductSkuDetailDto
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/12 14:07
 **/
@Data
public class ProductSkuDetailDto {
    private String skuName;//颜色、重量、尺寸。。。。等
    private String skuValue;//规格值：白色、100g、12cm(长)*15cm(宽)*5cm(高)。。。。等
}
