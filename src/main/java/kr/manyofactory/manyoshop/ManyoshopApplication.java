package kr.manyofactory.manyoshop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ManyoshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManyoshopApplication.class, args);
    }

}
