package com.msc.user.dto.resp;

import com.msc.user.dto.MenuDTO;
import com.msc.user.dto.UserDTO;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @ClassName LoginResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/13 10:58
 **/
@Data
public class LoginResp {

    private UserDTO sysUser;
    private List<MenuDTO> menuList;
    private String token;
}
