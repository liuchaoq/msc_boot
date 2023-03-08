package com.msc.user.service.impl;

import com.msc.common.base.PageEntity;
import com.msc.common.constant.CmsConstant;
import com.msc.common.constant.DictConstant;
import com.msc.common.util.DateUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.util.UUIDGenerator;
import com.msc.common.vo.Result;
import com.msc.common.weixin.TemplateMessageUtil;
import com.msc.common.weixin.TokenUtil;
import com.msc.common.weixin.WeiXinConstant;
import com.msc.common.weixin.entity.WxTemplateMessage;
import com.msc.common.weixin.entity.WxTemplateMessageDetail;
import com.msc.common.weixin.entity.WxTemplateMessageDetailContent;
import com.msc.user.dto.req.MeterListReq;
import com.msc.user.dto.req.MeterReq;
import com.msc.user.dto.req.StatisticsReq;
import com.msc.user.dto.resp.MeterListResp;
import com.msc.user.dto.resp.MyRecordsResp;
import com.msc.user.entity.*;
import com.msc.user.mapper.*;
import com.msc.user.service.MeterRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msc.user.service.WhProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuCq
 * @since 2022-07-08
 */
@Transactional
@Slf4j
@Service
public class MeterRecordServiceImpl extends ServiceImpl<MeterRecordMapper, MeterRecord> implements MeterRecordService {

    @Autowired
    private MeterRecordMapper meterRecordMapper;
    @Autowired
    private TOrderMapper tOrderMapper;
    @Autowired
    private WhProductService whProductService;
    @Autowired
    private TOrderProductMapper tOrderProductMapper;
    @Autowired
    private THouseInfoMapper tHouseInfoMapper;
    @Autowired
    private TOrderPaymentMapper tOrderPaymentMapper;

    @Value("${tempelet.model.ordernotice}")
    private String orderNoticeTemplateId;
    @Value("${base.system.url}")
    private String baseSystemUrl;
    @Resource
    private RedisUtil redisUtil;
    /**
    * @Description: 水站负责人保存抄表记录，需校验是否有权限抄表
     * 创建待支付订单、推送给用户水表待支付信息
    * @Param: [sysUser, meterReq]
    * @return: com.msc.common.vo.Result<java.lang.Object>
    * @Author: liuCq
    * @Date: 2022/7/8
    */
    @Override
    public Result<Object> saveRecords(SysUser sysUser, MeterReq meterReq) {
//        if (CollectionUtils.isEmpty(meterReq.getImgs())) {
//            return Result.error("请对水表拍照保存");
//        }
        if (meterReq.getLastNum() == null) {
            return Result.error("请填写当前水表读数");
        }
        if (meterReq.getLastNum() == null) {
            return Result.error("请填写上次水表读数");
        }
        if (meterReq.getLastNum() != null && meterReq.getNum() < meterReq.getLastNum()) {
            return Result.error("当前水表读数不可小于上次读数");
        }
        //根据负责人id获取当前水费标准
        WhProduct whProduct = whProductService.getWaterPriceByUser(sysUser);
        if (whProduct == null) {
            return Result.error("未设置水费收费标准,请联系管理员");
        }
        Date createTime = new Date();
        try {
            MeterRecord meterRecord = new MeterRecord();
            BeanUtils.copyProperties(meterReq,meterRecord);
            if (CollectionUtils.isNotEmpty(meterReq.getImgs())) {
                meterRecord.setImg(meterReq.getImgs().get(0));
            }
            meterRecord.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
            meterRecord.setYear(Calendar.getInstance().get(Calendar.YEAR));
            meterRecord.setAmount(whProduct.getPrice().multiply(new BigDecimal(meterReq.getNum()-meterReq.getLastNum())));
            meterRecord.setPrice(whProduct.getPrice());
            meterRecord.setUseCount(meterReq.getNum()-meterReq.getLastNum());
            meterRecord.setCreateTime(createTime);
            meterRecord.setCreateUserId(sysUser.getId().intValue());
            meterRecord.setProductCode(whProduct.getCode());
            meterRecordMapper.insert(meterRecord);


            TOrder order = new TOrder();
            order.setCode(UUIDGenerator.generate());
            order.setCreateTime(createTime);
            order.setCreateUserId(sysUser.getId().intValue());
            order.setOrderAmount(meterRecord.getAmount());
            order.setCreateUserId(sysUser.getId().intValue());
            order.setOrderType(0);
            order.setPayStatus(0);
            order.setHouseId(meterReq.getHouseId());
            order.setUserId(meterRecord.getHouseId());
            tOrderMapper.insert(order);

            TOrderProduct tOrderProduct = new TOrderProduct();
            tOrderProduct.setAmount(meterRecord.getAmount());
            tOrderProduct.setCreateTime(createTime);
            tOrderProduct.setCreateUserId(sysUser.getId().intValue());
            tOrderProduct.setOrderCode(order.getCode());
            tOrderProduct.setPrice(whProduct.getPrice());
            tOrderProduct.setProductCode(whProduct.getCode());
            tOrderProduct.setProductSkuCount(meterRecord.getUseCount());
            tOrderProduct.setProductSkuId(meterRecord.getId());
            //0已创建1待发货2已发货3已送达4已签收5待退货6已退货
            tOrderProduct.setStatus(0);
            tOrderProductMapper.insert(tOrderProduct);

            //创建推送消息
            List<SysUser> users = tHouseInfoMapper.getUsersByHouseId(meterRecord.getHouseId());
            CompletableFuture.runAsync(()->{
                for (SysUser user: users) {
                    WxTemplateMessage wxTemplateMessage = new WxTemplateMessage();
                    wxTemplateMessage.setClient_msg_id(order.getId().toString());
                    wxTemplateMessage.setTemplate_id(orderNoticeTemplateId);
                    wxTemplateMessage.setTouser(user.getOpenId());
                    WxTemplateMessageDetail wxTemplateMessageDetail = new WxTemplateMessageDetail();
                    wxTemplateMessageDetail.setFirst(new WxTemplateMessageDetailContent("水费缴纳提醒"));
                    wxTemplateMessageDetail.setKeyword1(new WxTemplateMessageDetailContent(order.getCode()));
                    wxTemplateMessageDetail.setKeyword2(new WxTemplateMessageDetailContent(whProduct.getProductName()));
                    wxTemplateMessageDetail.setKeyword3(new WxTemplateMessageDetailContent(order.getOrderAmount().toString()));
                    wxTemplateMessageDetail.setKeyword4(new WxTemplateMessageDetailContent(DateUtil.Date2Str(createTime)));
                    wxTemplateMessage.setData(wxTemplateMessageDetail);
                    //跳转页面的后半段path
                    wxTemplateMessage.setUrl(baseSystemUrl + "wx/to/myRecords");
                    String token = (String) redisUtil.get(CmsConstant.TOKEN_NAME);
                    if (StringUtils.isEmpty(token)) {
                        token = TokenUtil.getToken().getAccess_token();
                    }
                    TemplateMessageUtil.sendTemplateMsg(wxTemplateMessage, token);
                }
            });
            return Result.OK();
        } catch (Exception e) {
            log.error("微信端抄表失败,负责人id:{},错误内容:{}",sysUser.getId().toString(),e.getMessage());
            return Result.error("抄表失败,请联系管理员");
        }
    }

    @Override
    public Result<Object> getRecordsByManger(SysUser sysUser, PageEntity pageEntity) {
        Map<String,Object> params = pageEntity.getParams();
        pageEntity.setOffSet((pageEntity.getStart()-1)*pageEntity.getSize());
        params.put("userId", sysUser.getId());
        List<MeterListResp> rows = meterRecordMapper.getRecordsByManger(pageEntity);
        if (CollectionUtils.isEmpty(rows)) {
            pageEntity.setPageCount(0);
        } else {
            pageEntity.setRows(rows);
//            pageEntity.setPageCount(meterRecordMapper.getPageCount(pageEntity).get(0).getPageCount());
            pageEntity.setTotal(meterRecordMapper.getPageCount(pageEntity).get(0).getTotal());
        }
        return Result.OK(pageEntity);
    }

    @Override
    public Result<Object> getMeterRecordsByUser(SysUser sysUser, StatisticsReq statisticsReq) {
        try {
            List<MyRecordsResp> records = meterRecordMapper.getMeterRecordsByUser(sysUser.getId(), statisticsReq.getYear());
            return Result.OK(records);
        } catch (Exception e) {
            log.error("用户获取水费使用记录失败,用户id:{},错误内容:{}",sysUser.getId(),e.getMessage());
            return Result.error("获取用水记录失败，请重试");
        }
    }

    @Override
    public Result<Object> offlinePayByManager(SysUser sysUser, int meterRecordId) {
        PageEntity pageEntity = new PageEntity();
        Map<String,Object> params = new HashMap<>();
        params.put("userId", sysUser.getId());
        params.put("id",meterRecordId);
        pageEntity.setParams(params);
        List<MeterListResp> rows = meterRecordMapper.getRecordsByManger(pageEntity);
        if (CollectionUtils.isEmpty(rows)) {
            return Result.ERROR("只有所属管理员可以操作线下收费");
        }
        MeterRecord meterRecord = meterRecordMapper.selectById(meterRecordId);
        meterRecord.setPayStatus(CmsConstant.PAY_STATUS_YES);
        meterRecord.setUpdateTime(new Date());
        meterRecord.setUpdateUserId(sysUser.getId().intValue());
        //将抄表记录状态更新为已收费
        meterRecordMapper.updateById(meterRecord);
        //通过抄表记录查找订单产品与订单，更新订单支付状态
        TOrder tOrder = tOrderMapper.getOrderInfoByMeterId(meterRecordId);
        tOrder.setPayStatus(CmsConstant.PAY_STATUS_YES);
        tOrder.setUpdateTime(new Date());
        tOrder.setUpdateUserId(sysUser.getId().intValue());
        tOrderMapper.updateById(tOrder);
        //创建支付记录
        //创建订单支付记录
        TOrderPayment payment = new TOrderPayment();
        payment.setAmount(tOrder.getOrderAmount());
        payment.setOrderCode(tOrder.getCode());
        payment.setCode(UUIDGenerator.generate());
        payment.setCreateTime(new Date());
        payment.setCreateUserId(tOrder.getUserId());
        payment.setPayStatus(CmsConstant.PAY_STATUS_YES);
        payment.setOpenId(sysUser.getOpenId());
        tOrderPaymentMapper.insert(payment);

        return Result.OK("线下支付成功");
    }
}
