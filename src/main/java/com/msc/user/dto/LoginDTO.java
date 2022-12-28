package com.msc.user.dto;

/**
 * @ClassName LoginDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/15 10:41
 **/

import com.msc.user.dto.resp.LoginResp;
import com.msc.user.entity.SysMenu;
import com.msc.user.entity.SysRole;
import com.msc.user.entity.SysUser;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName LoginResp
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/13 10:58
 **/
@Data
public class LoginDTO extends SysUser {
    private List<SysMenu> menuList;
    private List<SysRole> roleList;

    public LoginResp getLoginResp() {
        LoginResp loginResp = new LoginResp();
        UserDTO sysUser = new UserDTO();
        BeanUtils.copyProperties(this, sysUser);
        loginResp.setSysUser(sysUser);
        List<MenuDTO> first = this.menuList.stream().filter(v->v.getParentId() == 0).map(v->{
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(v, menuDTO);
            return menuDTO;
        }).collect(Collectors.toList());
        Map<Integer, List<SysMenu>> map = this.menuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        first.forEach(v->{
            if (CollectionUtils.isNotEmpty(map.get(v.getId()))) {
                v.setChildren(map.get(v.getId()).stream().map(v1->{
                    MenuDTO menuDTO = new MenuDTO();
                    BeanUtils.copyProperties(v1, menuDTO);
                    return menuDTO;
                }).collect(Collectors.toList()));
            }
        });
        loginResp.setMenuList(first);
        return loginResp;
    }

    public SysUser getSysUser() {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(this, sysUser);
        return sysUser;
    }
}
