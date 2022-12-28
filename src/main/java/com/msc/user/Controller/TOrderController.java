package com.msc.user.Controller;

import com.msc.common.base.BaseController;
import com.msc.common.base.PageEntity;
import com.msc.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TOrderController
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/8/8 14:23
 **/
@RestController
public class TOrderController extends BaseController {

    @PostMapping("/order/list.do")
    public Result<Object> getOrderList(@RequestBody PageEntity pageEntity) {
        return Result.OK(pageEntity);
    }
}
