package com.msc.user.mapper;

import com.msc.user.dto.req.WxSaveUserReq;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.THouseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 家户水表信息 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-28
 */
@Repository
public interface THouseInfoMapper extends BaseMapper<THouseInfo> {

    List<THouseInfo> getHouseListByManagerAndVId(@Param("userId") Long userId, @Param("vId") Integer villageId);

    List<SysUser> getUsersByHouseId(@Param("houseId") Integer houseId);

    Integer updateHomeNo(@Param("houseInfo") WxSaveUserReq wxSaveUserReq);
}
