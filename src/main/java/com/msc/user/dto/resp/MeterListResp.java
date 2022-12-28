package com.msc.user.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName MeterListResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/13 16:02
 **/
@Data
public class MeterListResp {
    @ApiModelProperty(value = "住户id")
    private Integer houseId;

    @ApiModelProperty(value = "使用数量")
    private Integer useCount;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付状态0未支付1已支付")
    private Integer payStatus;

    private String address;//地址：县、乡、镇、村名、门牌号

    private String members;//家庭成员，中间逗号分割

}
