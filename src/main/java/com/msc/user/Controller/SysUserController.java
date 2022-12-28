package com.msc.user.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.util.JgMessageUtil;
import com.msc.common.util.PasswordUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.encryption.AesEncryptUtil;
import com.msc.common.util.encryption.EncryptedString;
import com.msc.common.vo.Result;
import com.msc.common.weixin.TokenUtil;
import com.msc.user.dto.jgmessage.AuthCodeDTO;
import com.msc.user.dto.req.LoginReq;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import com.msc.user.mapper.SysRoleMapper;
import com.msc.user.service.SysRoleService;
import com.msc.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuCq
 * @since 2022-04-12
 */
@Slf4j
@RestController
public class SysUserController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/sys/user/list.do")
    public Result<Object> getUserList(HttpServletRequest request) {
        return sysUserService.getUserList(getUser().getId(), new PageEntity(request));
    }

    @GetMapping("/sys/user/getUserById.do")
    public Result<Object> getUserById(@RequestParam("id") Integer id) {
        return sysUserService.getUserById(getUser().getId(), id);
    }

    @GetMapping("/test1")
    public SysUser test1(@RequestParam(required = true) String content, HttpServletRequest request) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        return sysUserService.test(content);
    }

    @PostMapping("/sys/login/login.do")
    @ResponseBody
    public Result<Object> login(@RequestBody LoginReq loginReq, HttpServletRequest request) {
        if (loginReq == null || StringHelper.isNullOrEmptyString(loginReq.getCode())
                || StringHelper.isNullOrEmptyString(loginReq.getPassword())) {
            return Result.error("登录信息填写不正确!");
        }
        return sysUserService.login(loginReq, request);
    }

    @GetMapping("/sys/login/checkToken.do")
    @ResponseBody
    public Result<Object> checkToken() {
        if (getUser() != null) {
            return Result.OK();
        }
        return Result.error("登录已过期,请从新登录");
    }

    @PostMapping("/sys/user/saveUserRole.do")
    @ResponseBody
    public Result<Object> saveUserRole(@RequestBody SysUser sysUser) {
        SysUser manager = getUser();
        sysUser.setOId(manager.getOId());
        log.error("获取信息:" + JSON.toJSONString(sysUser));
        if (!CollectionUtils.isEmpty(sysUser.getRoleNames())) {
            return sysRoleService.saveUserRole(sysUser);
        } else {
            return Result.error("请选择要分配的角色");
        }
    }

    @GetMapping("/sys/user/getMySysInfo.do")
    @ResponseBody
    public Result<Object> getMySysInfo() {
        SysUser user = getUser();
        Result<Object> roleResult = sysRoleService.getRoleList(null,0L+getUser().getOId(),null);
        List<SysRole> rows = (List<SysRole>) roleResult.getResult();
        user.setRoles(rows.stream().map(SysRole::getName).collect(Collectors.toList()));

        user.setAddressList(sysUserService.getMyAddressInfos(user));
        return Result.OK(user);
    }

    /**
     * type 2.修改密码获取验证码
    **/
    @GetMapping("/sys/user/getAuthCode.do")
    public Result<Object> getAuthCode(@RequestParam("type") Integer type) {
        SysUser sysUser = getUser();
        String mobile = sysUser.getUsername();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", mobile);
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(1000,"未获取到您所验证的手机号码");
        }
        String key = TokenUtil.getRedisKey(type, sysUser);
        Integer code = TokenUtil.getAuthCode();
        redisUtil.set(key, code, 600);
        AuthCodeDTO authCodeDTO = new AuthCodeDTO(mobile, code);
        //极光发送手机验证码
        return JgMessageUtil.sendAuthCOde(authCodeDTO);
    }

    @PostMapping("/sys/user/resetPassword.do")
    @ResponseBody
    public Result<Object> resetPassword(@RequestBody JSONObject jsonObject) throws Exception {
        SysUser user = getUser();
        EncryptedString encryptedString = user.getAesData();
        String info = AesEncryptUtil.desEncrypt(jsonObject.getString("info"), encryptedString.getKey(), encryptedString.getIv(), AesEncryptUtil.NO_PADDING);
        Map<String,String> infos = JSON.parseObject(info, Map.class);

        String key = TokenUtil.getRedisKey(2, user);
        if (!redisUtil.hasKey(key) || !infos.get("checkCode").equals(redisUtil.get(key).toString())) {
            return Result.error("验证码校验失败");
        }
        if (!infos.get("password").equals(infos.get("passwordR"))) {
            return Result.error("两次密码输入不一致，修改密码失败");
        }
        String password = PasswordUtil.encrypt(infos.get("password"));
        user.setPassword(password);
        sysUserService.resetPassword(user);
        return Result.OK();
    }
}
