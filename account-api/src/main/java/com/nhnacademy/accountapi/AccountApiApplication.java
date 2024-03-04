package com.nhnacademy.accountapi;

import com.nhnacademy.accountapi.properties.PropertiesBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {PropertiesBase.class})
public class AccountApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApiApplication.class, args);
    }

}
