package com.msc.common.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @ClassName AlicloudOSSProperties
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/5 13:14
 **/
@Slf4j
@Data
@EnableCaching
public class AliCloudOSSProperties {

    @Value("${file.alicloud.bucket-name}")
    private String bucketName;

    @Value("${file.alicloud.access-key}")
    private String accessKey;

    @Value("${file.alicloud.secret-key}")
    private String secretKey;

    @Value("${file.alicloud.endpoint}")
    private String endpoint;

}
