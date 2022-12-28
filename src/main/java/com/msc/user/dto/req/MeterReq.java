package com.msc.user.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MeterReq
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/8 17:27
 **/
@Data
public class MeterReq implements Serializable {

    private Integer villageId;//:'',
    private Integer houseId;//:'',
    private List<String> imgs;//:[],
    private Integer lastNum;//:'',

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastCopyTime;//:'',
    private Integer num;//:'',
}
