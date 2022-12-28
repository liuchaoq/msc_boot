package com.msc.user.mapper;

import com.msc.user.entity.WhProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品规格表    交易订单主要依靠此表的操作来进行生成 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-12
 */
@Repository
public interface WhProductSkuMapper extends BaseMapper<WhProductSku> {

}
