package com.msc.user.dto.req;

import com.msc.user.dto.ProductSkuDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName WhProductReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/12 10:10
 **/
@Data
public class WhProductReq {
    private String description;//: ""
    private Integer shelvesStatus;//: 是否上架
    private Integer id;//: ""
    private String intro;//: "<p>按照武陟县生活收费标准</p>"
    private BigDecimal price;//: "1.5"
    private String productName;//: "大油村水站水费标准"
    private Integer productType;//: "1"
    private String reviewMessage;//: ""
    private Integer reviewStatus;//: ""
    private Integer shelvesType;//: ""
    private Integer showStatus;//: ""
    private List<ProductSkuDto> skuList;
}
