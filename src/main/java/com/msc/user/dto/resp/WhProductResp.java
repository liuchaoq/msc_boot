package com.msc.user.dto.resp;

import com.msc.user.dto.WhProductSkuDTO;
import com.msc.user.entity.WhProductSku;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName WhProductResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/27 16:36
 **/
@Data
public class WhProductResp {
    private String productName;
    private String code;
    private List<WhProductSkuDTO> skuList;
    private Date createTime;
    private String Status;
}
