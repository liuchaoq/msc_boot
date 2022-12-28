package com.msc.user.service;

import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.msc.user.entity.SysUser;

/**
 * <p>
 * 数据权限表 服务类
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-23
 */
public interface SysFunctionService extends IService<SysFunction> {

    Result<Object> selectList(SysUser sysUser, Integer id, PageEntity pageEntity);

    /** 
    * @Description: 校验当前登录用户是否拥有当前操作权限 
    * @Param: [id, id1] 
    * @return: boolean 
    * @Author: liuCq
    * @Date: 2022/6/23 
    */ 
    boolean checkState(Long id, Integer id1);
}
