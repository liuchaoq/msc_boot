package com.msc.common.weixin.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName WeiXinMsg
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 17:25
 **/
@Data
public class WeiXinMsg {
    private String ToUserName;	//开发者微信号
    private String FromUserName;	//发送方帐号（一个OpenID）
    private Date CreateTime;	//消息创建时间戳（整型）
    /**
    * 事件类型，event
    * 消息类型，文本为text
    * 消息类型，图片为image
     * 语音为voice
     * 视频为video
     * 小视频为shortvideo
     * 消息类型，地理位置为location
     * 消息类型，链接为link
    **/
    private String MsgType;
    /**
     * 事件类型
     * VIEW(点击菜单跳转链接时的事件推送)
     * CLICK（点击菜单拉取消息时的事件推送）
     * LOCATION（上报地理位置事件）
     * SCAN（用户已关注时的事件推送）扫描带参数二维码事件
     * subscribe（用户未关注时，进行关注后的事件推送）扫描带参数二维码事件
     * unsubscribe（取消关注事件）
     **/
    private String Event;
    private String EventKey;	//事件 KEY 值，设置的跳转URL
    private Double Latitude;	//地理位置纬度
    private Double Longitude;	//地理位置经度
    private Double Precision;	//地理位置精度
    private String Ticket;	//二维码的ticket，可用来换取二维码图片
    private String Title;	//消息标题
    private String Description;	//消息描述
    private String Url;	//消息链接
    private Long MsgId;	//消息id，64位整型
    private Long MsgDataId;	//消息的数据ID（消息如果来自文章时才有）
    private String Idx;	//多图文时第几篇文章，从1开始（消息如果来自文章时才有）
    private Double Location_X;	//地理位置纬度
    private Double Location_Y;	//地理位置经度
    private Double Scale;	//地图缩放大小
    private String Label;	//地理位置信息
    private Long MediaId;	//视频消息媒体id，可以调用获取临时素材接口拉取数据。
    private Long ThumbMediaId;	//视频消息缩略图的媒体id，可以调用获取临时素材接口拉取数据。
    private String Format;	//语音格式：amr
    private String Recognition;	//语音识别结果，UTF8编码
    private String PicUrl;	//图片链接（由系统生成）
    private String Content;	//文本消息内容
}
