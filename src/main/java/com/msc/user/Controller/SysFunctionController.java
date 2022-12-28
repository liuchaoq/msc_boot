package com.msc.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.BaseController;
import com.msc.common.base.FunctionTypeEnum;
import com.msc.common.base.PageEntity;
import com.msc.common.util.FunctionRequired;
import com.msc.common.vo.Result;
import com.msc.user.entity.*;
import com.msc.user.mapper.SysRoleFunctionMapper;
import com.msc.user.mapper.SysRoleMapper;
import com.msc.user.service.SysFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysFunctionController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/23 9:37
 **/
@Slf4j
@RestController
public class SysFunctionController extends BaseController {
    @Autowired
    private SysFunctionService sysFunctionService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleFunctionMapper sysRoleFunctionMapper;

    @GetMapping("/sys/function/list.do")
    public Result<Object> getFunctionList(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        Integer id = StringUtils.isEmpty(idStr)?null:Integer.parseInt(idStr);
        SysUser sysUser = getUser();
        PageEntity pageEntity = id == null?new PageEntity(request):null;
        return sysFunctionService.selectList(sysUser, id, pageEntity);
    }

    @FunctionRequired(value = "FUNCTION_SAVE", type = FunctionTypeEnum.INSERT)
    @PostMapping("/sys/function/save.do")
    public Result<Object> saveFunction(@RequestBody SysFunction sysFunction) {
        if (sysFunction.getId() == null) {
            QueryWrapper<SysFunction> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", sysFunction.getCode());
            if (sysFunctionService.getOne(queryWrapper) != null) {
                return Result.error("该权限编号已存在,请确认");
            }
        }
        SysUser sysUser = getUser();
        if (sysFunction.getId() == null) {
            sysFunction.setCreateUserId(sysUser.getOId());
            sysFunction.setCreateTime(new Date());
        } else {
            sysFunction.setUpdateUserId(sysUser.getOId());
            sysFunction.setUpdateTime(new Date());
        }
        try {
            sysFunctionService.saveOrUpdate(sysFunction);
            QueryWrapper<SysRoleFunction> delWrapper = new QueryWrapper<>();
            delWrapper.eq("sys_function_id", sysFunction.getId());
            sysRoleFunctionMapper.delete(delWrapper);
            if (!CollectionUtils.isEmpty(sysFunction.getRoleNames())) {
                QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
                queryWrapper.in("name", sysFunction.getRoleNames());
                List<SysRole> roleList = sysRoleMapper.selectList(queryWrapper);
                if (!CollectionUtils.isEmpty(roleList)) {
                    roleList.stream().forEach(v->{
                        SysRoleFunction sysRoleFunction = new SysRoleFunction();
                        sysRoleFunction.setCreateTime(new Date());
                        sysRoleFunction.setCreateUserId(sysUser.getOId().intValue());
                        sysRoleFunction.setSysFunctionId(sysFunction.getId());
                        sysRoleFunction.setSysRoleId(v.getId());
                        sysRoleFunctionMapper.insert(sysRoleFunction);
                    });
                }
            }
            return Result.OK("操作成功!");
        } catch (Exception e) {
            log.error("保存权限报错:{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @FunctionRequired(value = "FUNCTION_DEL", type = FunctionTypeEnum.DELETE)
    @PostMapping("/sys/function/delete.do")
    public Result<Object> deleteFunction(@RequestParam("id") Integer id) {

        try {
            if (getUser().getId() != null && !sysFunctionService.checkState(getUser().getId(), id)) {
                return Result.error("您无权删除该权限!");
            }
            sysFunctionService.removeById(id);
            return Result.OK("操作成功!");
        } catch (Exception e) {
            log.error("保存权限报错:{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
