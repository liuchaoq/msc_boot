package com.msc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msc.common.base.PageEntity;
import com.msc.common.util.UUIDGenerator;
import com.msc.common.vo.Result;
import com.msc.user.dto.req.WhProductReq;
import com.msc.user.dto.resp.WhProductResp;
import com.msc.user.entity.SysUser;
import com.msc.user.entity.WhProduct;
import com.msc.user.entity.WhProductSku;
import com.msc.user.entity.WhProductSkuDetail;
import com.msc.user.mapper.WhProductMapper;
import com.msc.user.mapper.WhProductSkuDetailMapper;
import com.msc.user.mapper.WhProductSkuMapper;
import com.msc.user.service.WhProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产品表，水费（没有规格数据，price作为水费价格）、商品（必须有规格数据，价格按规格定价计算）,已审核通过的商品内容不可修改，如需修改，先下架，在发布一条修改过后的商品提交审核 服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Service
public class WhProductServiceImpl extends ServiceImpl<WhProductMapper, WhProduct> implements WhProductService {
    @Autowired
    private WhProductMapper whProductMapper;
    @Autowired
    private WhProductSkuMapper skuMapper;
    @Autowired
    private WhProductSkuDetailMapper skuDetailMapper;

    @Override
    public WhProduct getWaterPriceByUser(SysUser sysUser) {
        QueryWrapper<WhProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", sysUser.getId())
                .eq("product_type", 1)
                .eq("review_status",1)
                .eq("shelves_status",1)
                .orderByDesc("id")
                .last(" limit 1");
        return whProductMapper.selectOne(queryWrapper);
    }

    @Override
    public Result<Object> saveOrUpdateProduct(SysUser sysUser, WhProductReq whProduct) {
        String productCode = UUIDGenerator.generate();
        //保存产品信息
        WhProduct product = new WhProduct();
        BeanUtils.copyProperties(whProduct, product);
        product.setCode(productCode);
        product.setCreateTime(new Date());
        product.setCreateUserId(sysUser.getId().intValue());
        product.setOwnerId(sysUser.getId().intValue());
        whProductMapper.insert(product);

        if (!CollectionUtils.isEmpty(whProduct.getSkuList())) {
            whProduct.getSkuList().stream().forEach(v->{
                //保存规格
                WhProductSku sku = new WhProductSku();
                BeanUtils.copyProperties(v,sku);
                sku.setProductCode(productCode);
                sku.setCreateTime(new Date());
                sku.setCreateUserId(sysUser.getId().intValue());
                sku.setSellNum(v.getShelvesNum());
                skuMapper.insert(sku);
                //保存规格明细
                if (!CollectionUtils.isEmpty(v.getDetails())) {
                    v.getDetails().stream().forEach(v1->{
                        WhProductSkuDetail detail = new WhProductSkuDetail();
                        BeanUtils.copyProperties(v1,detail);
                        detail.setCreateTime(new Date());
                        detail.setCreateUserId(sysUser.getId().intValue());
                        detail.setProductSkuId(sku.getId());
                        skuDetailMapper.insert(detail);
                    });
                }
            });
        }
        return Result.OK();
    }

    @Override
    public Result<Object> productList(String productCode, Long userId, PageEntity pageEntity) {
        List<WhProductResp> rows = whProductMapper.productList(productCode, userId, pageEntity);
        if (pageEntity != null && pageEntity.getStart() > 0) {

        }
        return null;
    }
}
