package com.msc.user.Controller;

import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import com.msc.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysRoleController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:15
 **/
@RestController
@Slf4j
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/sys/role/roleList.do")
    public Result<Object> getRoleList(HttpServletRequest request) {
        Integer roleId = null;
        if (!StringHelper.isNullOrEmptyString(request.getParameter("roleId"))) {
            roleId = Integer.parseInt(request.getParameter("roleId"));
        }
        SysUser sysUser = getUser(request);
        PageEntity pageEntity = new PageEntity(request);
        return sysRoleService.getRoleList(roleId, sysUser.getId(), pageEntity);
    }

    @PostMapping("/sys/role/save.do")
    public Result<Object> saveRole(@RequestBody SysRole sysRole, HttpServletRequest request) {
        SysUser sysUser = getUser(request);
        if (sysRole.getId() == null) {
            sysRole.setCreateTime(new Date());
            sysRole.setCreateUserId(sysUser.getOId().intValue());
        } else {
            sysRole.setUpdateTime(new Date());
            sysRole.setUpdateUserId(sysUser.getOId().intValue());
        }
        return sysRoleService.saveRole(sysRole);
    }

    @PostMapping("/sys/role/delete.do")
    public Result<Object> deleteRole(@RequestParam Integer id, HttpServletRequest request) {
//        SysUser sysUser = getUser(request);
//        if (sysRole.getId() == null) {
//            sysRole.setCreateTime(new Date());
//            sysRole.setCreateUserId(sysUser.getOId().intValue());
//        } else {
//            sysRole.setUpdateTime(new Date());
//            sysRole.setUpdateUserId(sysUser.getOId().intValue());
//        }
        return sysRoleService.deleteRole(id);
    }
}
