package org.k1den.configclientproject;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class ConfigClientProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientProjectApplication.class, args);
    }

}
