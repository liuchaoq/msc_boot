package com.msc.user.Controller;

import com.msc.common.base.BaseController;
import com.msc.common.base.FunctionTypeEnum;
import com.msc.common.base.PageEntity;
import com.msc.common.util.FunctionRequired;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysUser;
import com.msc.user.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName SysMenuController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 18:33
 **/
@RestController
@Slf4j
public class SysMenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;
    @FunctionRequired(value = "MENU_LIST", type = FunctionTypeEnum.GET)
    @GetMapping("/sys/menu/menuList.do")
    public Result<Object> getMenuList(HttpServletRequest request){
        Integer menuId = null;
        String menuStr = request.getParameter("menuId");
        if (StringUtils.isNotBlank(menuStr)) {
            menuId = Integer.parseInt(menuStr);
        }
        PageEntity pageEntity = null;
        if (menuId == null) {
            pageEntity = new PageEntity(request);
        }
        SysUser sysUser = getUser(request);
        return sysMenuService.getMenuList(menuId, sysUser.getId(), pageEntity);
    }

    @GetMapping("/sys/menu/parent.do")
    public Result<Object> getParentList(HttpServletRequest request){
        SysUser sysUser = getUser(request);
        return sysMenuService.getParentMenuList(0, sysUser.getId(), null);
    }

    @GetMapping("/sys/menu/mainMenu.do")
    public Result<Object> getMainMenuList(HttpServletRequest request){
        SysUser sysUser = getUser(request);
        return sysMenuService.getMainMenuList(sysUser.getId());
    }

    @PostMapping("/sys/menu/save.do")
    public Result<Object> saveMenu(@RequestBody SysMenu sysMenu, HttpServletRequest request){
        SysUser sysUser = getUser(request);
        if (sysMenu.getId() == null) {
            sysMenu.setCreateTime(new Date());
            sysMenu.setCreateUserId(sysUser.getOId().intValue());
        } else {
            sysMenu.setUpdateTime(new Date());
            sysMenu.setUpdateUserId(sysUser.getOId().intValue());
        }
        return sysMenuService.saveOrUpdate(sysMenu);
    }

    @PostMapping("/sys/menu/delete.do")
    public Result<Object> deleteMenu(@RequestParam Integer id, HttpServletRequest request){
        SysUser sysUser = getUser(request);
//        if (sysUser.getId() == null) {
//            sysMenu.setCreateTime(new Date());
//            sysMenu.setCreateUserId(sysUser.getOId().intValue());
//        }
        return sysMenuService.deleteMenu(id);
    }
}
