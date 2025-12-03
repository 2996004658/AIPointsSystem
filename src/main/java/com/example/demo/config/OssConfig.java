package com.example.demo.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 * 读取配置文件中的OSS参数并创建OSSClient实例
 */
@Configuration
public class OssConfig {

    /**
     * OSS服务端点
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 访问密钥ID
     */
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    /**
     * 访问密钥Secret
     */
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 存储桶名称
     */
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 文件URL前缀
     */
    @Value("${aliyun.oss.urlPrefix}")
    private String urlPrefix;

    /**
     * 创建OSS客户端Bean
     * 
     * @return OSS客户端实例
     */
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获取存储桶名称
     * 
     * @return 存储桶名称
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 获取URL前缀
     * 
     * @return URL前缀
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }
}
