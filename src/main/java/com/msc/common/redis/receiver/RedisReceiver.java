package com.msc.common.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import com.msc.common.base.BaseMap;
import com.msc.common.constant.GlobalConstants;
import com.msc.common.redis.listener.BsdRedisListener;
import com.msc.common.util.SpringContextHolder;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        BsdRedisListener messageListener = SpringContextHolder.getHandler(handlerName.toString(), BsdRedisListener.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
