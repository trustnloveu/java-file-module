package kr.co.ejyang.module_file;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FileApplication {

    @Value("${profile.activate}")
    private String profile;

    @Value("${server.port}")
    private String port;

    @Value("${storage.endpoint}")
    private String storageEndPoint;

    public static void main(String[] args) { SpringApplication.run(FileApplication.class, args); }

    @PostConstruct
    private void start() {
        System.out.println("Profile ::: " + profile);
        System.out.println("Port ::: " + port);
        System.out.println("Storage Endpoint ::: " + storageEndPoint);
    }
}
