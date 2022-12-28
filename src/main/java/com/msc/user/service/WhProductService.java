package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.WhProductReq;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.WhProduct;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核 服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
public interface WhProductService extends IService<WhProduct> {

    WhProduct getWaterPriceByUser(SysUser sysUser);

    Result<Object> saveOrUpdateProduct(SysUser sysUser, WhProductReq whProduct);

    Result<Object> productList(String productCode, Long id, PageEntity pageEntity);
}
