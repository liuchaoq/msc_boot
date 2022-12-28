package com.msc.user.mapper;

import com.msc.user.entity.TUserHouseRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 住址与用户关系表，一条住址信息可能关联多个用户，一个用户可能关联多处地址 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-30
 */
@Repository
public interface TUserHouseRelationMapper extends BaseMapper<TUserHouseRelation> {

    Integer deleteHouseInfo(@Param("userId") Long id, @Param("houseInfoId") Integer houseInfoId);
}
