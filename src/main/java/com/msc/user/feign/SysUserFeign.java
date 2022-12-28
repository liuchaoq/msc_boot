package com.msc.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SysUserFeign
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/4/12 18:44
 **/
@FeignClient("msc-user")
public interface SysUserFeign {
}
