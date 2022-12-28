package com.msc.user.service.impl;

import com.msc.user.entity.WhProductSku;
import com.msc.user.mapper.WhProductSkuMapper;
import com.msc.user.service.WhProductSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品规格表    交易订单主要依靠此表的操作来进行生成 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-12
 */
@Service
public class WhProductSkuServiceImpl extends ServiceImpl<WhProductSkuMapper, WhProductSku> implements WhProductSkuService {

}
