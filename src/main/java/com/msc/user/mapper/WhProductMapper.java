package com.msc.user.mapper;

import com.msc.common.base.PageEntity;
import com.msc.user.dto.resp.WhProductResp;
import com.msc.user.entity.WhProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Repository
public interface WhProductMapper extends BaseMapper<WhProduct> {

    List<WhProductResp> productList(@Param("productCode") String productCode, @Param("userId") Long userId, @Param("page") PageEntity pageEntity);
}
