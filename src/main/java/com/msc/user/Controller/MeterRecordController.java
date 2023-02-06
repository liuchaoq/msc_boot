package com.msc.user.Controller;


import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.util.encryption.AesEncryptUtil;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.MeterReq;
import com.msc.user.dto.resp.UserManagerVillageResp;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.TVillageInfo;
import com.msc.user.service.MeterRecordService;
import com.msc.user.service.SysUserService;
import com.msc.user.service.TVillageInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@RestController
public class MeterRecordController extends BaseController {
    @Autowired
    private MeterRecordService meterRecordService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TVillageInfoService tVillageInfoService;
    /**
     *  保存抄表记录
     **/
    @PostMapping("/wx/saveMeterRecord.json")
    public Result<Object> saveMeterRecord(@RequestBody MeterReq meterReq) {
        SysUser sysUser = getWxUser();
        if (sysUser == null) {
           return Result.error("您无权访问该接口");
        }
        //校验当前接口访问者是否有对该村子的管理职责
        Result<Object> result = tVillageInfoService.getManagerVillage(sysUser);
        List<UserManagerVillageResp> infoList = (List<UserManagerVillageResp>) result.getResult();
        if (CollectionUtils.isEmpty(infoList) || infoList.stream().noneMatch(v->v.getVillageId() == meterReq.getVillageId())) {
            return Result.error("您无权访问该接口");
        }
        return meterRecordService.saveRecords(sysUser, meterReq);
    }

    @PostMapping("wx/getRecordsByManger")
    public Result<Object> getRecordsByManger(@RequestBody PageEntity pageEntity) {
        SysUser sysUser = getWxUser();
        if (sysUser == null) {
            return Result.error("您无权访问该接口");
        }
        return meterRecordService.getRecordsByManger(sysUser,pageEntity);
    }

    /**
     * 管理员手工收费地址
     * id:抄表记录id
    **/
    @GetMapping("wx/offlinePayByManager")
    public Result<Object> offlinePayByManager(@RequestParam("id") String id) throws Exception {
        SysUser sysUser = getWxUser();
        String idStr = AesEncryptUtil.desEncrypt(id, sysUser.getAesData().getKey(), sysUser.getAesData().getIv(),AesEncryptUtil.NO_PADDING);
        return meterRecordService.offlinePayByManager(sysUser,Integer.parseInt(idStr.trim()));
    }
}
