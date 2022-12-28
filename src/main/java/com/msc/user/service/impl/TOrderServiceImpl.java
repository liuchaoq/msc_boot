package com.msc.user.service.impl;

import com.msc.user.entity.TOrder;
import com.msc.user.mapper.TOrderMapper;
import com.msc.user.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-11
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

}
