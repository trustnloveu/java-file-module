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
@PropertySource("classpath:application-file-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private long maxSize; // 50 MB, 최대 사이즈
    private long minSize; // 1 KB, 최소 사이즈
    private long maxLength; // 80, 최대 파일명
    private String endPoint; // 80, 최대 파일명

//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "file")
//    public FileProperties fileProperties() {
//        return new FileProperties();
//    }

//    // FileProperties
//    public static class FileProperties {
//        private volatile long maxSize;
//
//        public void setMaxSize(String maxSize) {
//            this.maxSize = Long.parseLong(maxSize);
//        }
//
//        public long getMaxSize() {
//            return maxSize;
//        }
//    }

}
