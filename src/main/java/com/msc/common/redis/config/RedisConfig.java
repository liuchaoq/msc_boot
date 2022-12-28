package com.msc.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.msc.common.constant.CacheConstant;
import com.msc.common.constant.GlobalConstants;
import com.msc.common.redis.receiver.RedisReceiver;
import com.msc.common.redis.writer.BsdRedisCacheWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

import static java.util.Collections.singletonMap;

/**
* 开启缓存支持
* @author zyf
 * @Return:
*/
@Slf4j
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {


	/*
	 * 读取配置文件里的redis配置
	 */
	@Value("${redis.cache.database}")
	private Integer cacheDatabaseIndex;

	//sessiondb的数据库索引
	@Value("${redis.session.database}")
	private Integer sessionDatabaseIndex;

	@Value("${redis.host}")
	private String hostName;

	@Value("${redis.port}")
	private Integer port;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.lettuce.pool.max-idle}")
	private Integer maxIdle;

	@Value("${redis.lettuce.pool.min-idle}")
	private Integer minIdle;

	@Value("${redis.lettuce.pool.max-active}")
	private Integer maxActive;

	@Value("${redis.lettuce.pool.max-wait}")
	private Long maxWait;

	@Value("${redis.timeout}")
	private Long timeOut;

	@Value("${redis.lettuce.shutdown-timeout}")
	private Long shutdownTimeOut;

	/**
	 * RedisTemplate配置
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(value = "redisTemplate")
	public RedisTemplate<String, Object> cacheRedisTemplate() {
		log.info(" --- redis config init --- ");
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =jacksonSerializer();
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		LettuceConnectionFactory lettuceConnectionFactory =
				createLettuceConnectionFactory
						(cacheDatabaseIndex,hostName,port,password,maxIdle,minIdle,maxActive,maxWait,timeOut,shutdownTimeOut);
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		RedisSerializer<?> stringSerializer = new StringRedisSerializer();
		// key序列化
		redisTemplate.setKeySerializer(stringSerializer);
		// value序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// Hash key序列化
		redisTemplate.setHashKeySerializer(stringSerializer);
		// Hash value序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 缓存配置管理器
	 *
	 * @param factory
	 * @return
	 */
	@Bean
	public CacheManager cacheManager() {
        // 配置序列化（缓存默认有效期 6小时）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(6));
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                												.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		LettuceConnectionFactory factory = createLettuceConnectionFactory
				(cacheDatabaseIndex,hostName,port,password,maxIdle,minIdle,maxActive,maxWait,timeOut,shutdownTimeOut);
        // 以锁写入的方式创建RedisCacheWriter对象
		//update-begin-author:taoyan date:20210316 for:注解CacheEvict根据key删除redis支持通配符*
		RedisCacheWriter writer = new BsdRedisCacheWriter(factory, Duration.ofMillis(50L));
		//RedisCacheWriter.lockingRedisCacheWriter(factory);
		// 创建默认缓存配置对象
		/* 默认配置，设置缓存有效期 1小时*/
		//RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
		/* 自定义配置test:demo 的超时时间为 5分钟*/
		RedisCacheManager cacheManager = RedisCacheManager.builder(writer).cacheDefaults(redisCacheConfiguration)
				.withInitialCacheConfigurations(singletonMap(CacheConstant.TEST_DEMO_CACHE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)).disableCachingNullValues()))
				.withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_RANKING, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
				.withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_PAGE_LIST, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
				.transactionAware().build();
		//update-end-author:taoyan date:20210316 for:注解CacheEvict根据key删除redis支持通配符*
		return cacheManager;
	}

	/**
	 * redis 监听配置
	 *
	 * @param redisConnectionFactory redis 配置
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(MessageListenerAdapter commonListenerAdapter) {
		LettuceConnectionFactory lettuceConnectionFactory =
				createLettuceConnectionFactory
						(cacheDatabaseIndex,hostName,port,password,maxIdle,minIdle,maxActive,maxWait,timeOut,shutdownTimeOut);
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(lettuceConnectionFactory);
		container.addMessageListener(commonListenerAdapter, new ChannelTopic(GlobalConstants.REDIS_TOPIC_NAME));
		return container;
	}


	@Bean
	MessageListenerAdapter commonListenerAdapter(RedisReceiver redisReceiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver, "onMessage");
		messageListenerAdapter.setSerializer(jacksonSerializer());
		return messageListenerAdapter;
	}

	private Jackson2JsonRedisSerializer jacksonSerializer() {
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		return jackson2JsonRedisSerializer;
	}


	/**
	 * 自定义LettuceConnectionFactory,这一步的作用就是返回根据你传入参数而配置的
	 * LettuceConnectionFactory，
	 * 也可以说是LettuceConnectionFactory的原理了，
	 * 后面我会详细讲解的,各位同学也可先自己看看源码

	 这里定义的方法 createLettuceConnectionFactory，方便快速使用
	 */
	private  LettuceConnectionFactory createLettuceConnectionFactory(
			int dbIndex, String hostName, int port, String password,
			int maxIdle,int minIdle,int maxActive,
			Long maxWait, Long timeOut,Long shutdownTimeOut){

		//redis配置
		RedisConfiguration redisConfiguration = new
				RedisStandaloneConfiguration(hostName,port);
		((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
		((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

		//连接池配置
		GenericObjectPoolConfig genericObjectPoolConfig =
				new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(maxIdle);
		genericObjectPoolConfig.setMinIdle(minIdle);
		genericObjectPoolConfig.setMaxTotal(maxActive);
		genericObjectPoolConfig.setMaxWaitMillis(maxWait);

		//redis客户端配置
		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
				builder =  LettucePoolingClientConfiguration.builder().
				commandTimeout(Duration.ofMillis(timeOut));

		builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
		builder.poolConfig(genericObjectPoolConfig);
		LettuceClientConfiguration lettuceClientConfiguration = builder.build();

		//根据配置和客户端配置创建连接
		LettuceConnectionFactory lettuceConnectionFactory = new
				LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
		lettuceConnectionFactory .afterPropertiesSet();

		return lettuceConnectionFactory;
	}
}
