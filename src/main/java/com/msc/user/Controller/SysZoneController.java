package com.msc.user.Controller;

import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.constant.CmsConstant;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.SysZone;
import com.msc.user.service.SysZoneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName SysZoneController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/22 14:12
 **/
@RestController
public class SysZoneController extends BaseController {
    @Autowired
    private SysZoneService sysZoneService;
    @GetMapping("/sys/zone/list.do")
    public Result<Object> getZoneList(HttpServletRequest request) {
        String parentCode = request.getParameter("parentCode");
        PageEntity pageEntity = new PageEntity(request);
        return sysZoneService.getZoneList(parentCode, pageEntity);
    }

    @PostMapping("/sys/zone/save.do")
    public Result<Object> saveZone(@RequestBody SysZone sysZone) {
        SysUser sysUser = getUser();
        if (sysZone.getId() == null) {
            sysZone.setCreateBy(Long.parseLong(sysUser.getOId().toString()));
            sysZone.setGmtCreate(new Date());
        } else {
            sysZone.setUpdateBy(Long.parseLong(sysUser.getOId().toString()));
            sysZone.setGmtModified(new Date());
        }
        return sysZoneService.saveZone(sysZone);
    }

    @PostMapping("/sys/zone/delete.do")
    public Result<Object> deleteZone(@RequestParam("id") Integer id) {
        return sysZoneService.deleteZone(id);
    }

    @GetMapping("/sys/zone/getZoneByParent")
    public Result<Object> getZoneByParent(HttpServletRequest request) {
        String parentCode = request.getParameter("parentCode");
        if (StringUtils.isEmpty(parentCode)) {
            parentCode = CmsConstant.ZONE_COUNTRY_CHINA_CODE;
        }
        return sysZoneService.getZoneList(parentCode, null);
    }
}
