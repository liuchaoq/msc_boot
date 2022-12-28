package com.msc.user.Controller;


import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TRepairRecord;
import com.msc.user.service.TRepairRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 在线报修记录 前端控制器
 * </p>
 *
 * @author LiuCq
 * @since 2022-06-24
 */
@RestController
public class TRepairRecordController extends BaseController {

    @Autowired
    private TRepairRecordService tRepairRecordService;

    @GetMapping("/wx/repair/list")
    public Result<Object> getRepairRecords(HttpServletRequest request) {
        Integer id = null;
        String repairStr = request.getParameter("id");
        if (StringUtils.isNotBlank(repairStr)) {
            id = Integer.parseInt(repairStr);
        }
        PageEntity pageEntity = null;
        if (id == null) {
            pageEntity = new PageEntity(request);
        }
        SysUser sysUser = getWxUser();
        return tRepairRecordService.getRepairListByManager(id, sysUser.getId(), pageEntity);
    }

}
