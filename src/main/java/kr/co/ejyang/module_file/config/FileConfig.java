package kr.co.ejyang.module_file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private String url;         // API URL
    private String endPoint;    // 파일 저장 경로

}
