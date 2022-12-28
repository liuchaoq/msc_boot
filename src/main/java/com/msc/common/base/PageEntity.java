package com.msc.common.base;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PageEntity
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/17 8:55
 **/
@Slf4j
@Data
public class PageEntity {
    private Integer start = 0;//当前页码
    private Integer size = 10;//每页条数
    private Integer pageCount = 1;//总页数
    private Integer offSet = 0;
    private List<?> rows;
    private Map<String, Object> params;
    public PageEntity(HttpServletRequest request) {
        try {
            this.start = Integer.parseInt(request.getParameter("start"));
            this.offSet = (this.start-1)*this.size;
            this.params = JSON.parseObject(request.getParameter("params"));
        } catch (Exception e) {
            log.error("获取分页参数失败！");
        }
    }
}
