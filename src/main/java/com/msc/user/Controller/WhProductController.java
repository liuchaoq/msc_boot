package com.msc.user.Controller;


import com.msc.common.base.BaseController;
import com.msc.common.base.FunctionTypeEnum;
import com.msc.common.base.PageEntity;
import com.msc.common.util.FunctionRequired;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.WhProductReq;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.WhProduct;
import com.msc.user.service.WhProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核 前端控制器
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@RestController
public class WhProductController extends BaseController {

    @Autowired
    private WhProductService whProductService;

    @GetMapping("wh/product/list.do")
    public Result<Object> productList(HttpServletRequest request) {
        SysUser sysUser = getUser();
        String productCode = request.getParameter("code");
        PageEntity pageEntity = new PageEntity(request);
        return whProductService.productList(productCode, sysUser.getId(), pageEntity);
    }

    /**
     *  创建水费标准接口
    **/
    @FunctionRequired(value = "WATER_PRODUCT_ADD", type = FunctionTypeEnum.INSERT)
    @PostMapping("wh/saveProductInfo.do")
    public Result<Object> saveProductInfo(@RequestBody WhProductReq whProduct) {
        SysUser sysUser = getUser();
        if (whProduct.getProductType() == 1) {
            return whProductService.saveOrUpdateProduct(sysUser, whProduct);
        } else {
            return Result.error("您无权发布该商品!");
        }
    }

    /**
     *  创建普通商品接口
    **/
    @FunctionRequired(value = "PRODUCT_ADD", type = FunctionTypeEnum.INSERT)
    @PostMapping("wh/saveNormalProductInfo.do")
    public Result<Object> saveNormalProductInfo(@RequestBody WhProductReq whProduct) {
        SysUser sysUser = getUser();
        if (whProduct.getProductType() == 2) {
            return whProductService.saveOrUpdateProduct(sysUser, whProduct);
        } else {
            return Result.error("您无权发布该商品!");
        }
    }
}
