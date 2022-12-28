package com.msc.common.redis.listener;

import com.msc.common.base.BaseMap;

/**
 * 自定义消息监听
 */
public interface BsdRedisListener {

    void onMessage(BaseMap message);

}
