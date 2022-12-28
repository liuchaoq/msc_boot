package com.msc.user.dto.resp;

import com.msc.user.dto.MyRecordsDetailDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName MyRecordsResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/15 16:35
 **/
@Data
public class MyRecordsResp {
    private Integer month;
    private BigDecimal amount;
    private Integer payStatus;
    private List<MyRecordsDetailDto> details;
}
