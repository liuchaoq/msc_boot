package com.msc.user.mapper;

import com.msc.user.dto.resp.UserManagerVillageResp;
import com.msc.user.entity.TUserVillageManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 水站管理员所管村子 Mapper 接口
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-29
 */
@Repository
public interface TUserVillageManagerMapper extends BaseMapper<TUserVillageManager> {

    List<UserManagerVillageResp> getManagerVillage(@Param("userId") Long userId);

    List<HashMap<String, Object>> getManagerList(@Param("managerId") Long managerId, @Param("roleId") Integer roleId);
}
