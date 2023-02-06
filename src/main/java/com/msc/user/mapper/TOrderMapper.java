package com.msc.user.mapper;

import com.msc.user.dto.OrderInfoDTO;
import com.msc.user.entity.TOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单主表 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-11
 */
@Repository
public interface TOrderMapper extends BaseMapper<TOrder> {

    OrderInfoDTO getOrderInfo(@Param("orderCode") String orderCode);

    /**
    * @Description:  根据订单编号更新水费支付情况
    * @Param: [code, 订单编号
     * status 更新后的状态
     * ]
    * @return: java.lang.Integer
    * @Author: liuCq
    * @Date: 2022/7/22
    */
    Integer modifyMeterRecordStatus(@Param("orderCode") String code, @Param("status") Integer status);

    TOrder getOrderInfoByMeterId(@Param("meterId") int meterRecordId);
}
