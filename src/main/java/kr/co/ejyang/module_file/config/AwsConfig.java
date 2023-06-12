package kr.co.ejyang.module_file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "aws")
public class AwsConfig {

//    private String endPoint; // 파일 저장 경로
//
//    private long maxSize; // 50 MB, 최대 사이즈
//    private long minSize; // 1 KB, 최소 사이즈
//    private long maxLength; // 80, 최대 파일명

}
