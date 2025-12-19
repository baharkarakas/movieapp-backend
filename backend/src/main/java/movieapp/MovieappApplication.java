package movieapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieappApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieappApplication.class, args);
    }

    @Bean
    CommandLineRunner testProps(@Value("${app.jwt.secret:NOT_FOUND}") String secret) {
        return args -> System.out.println("JWT SECRET = " + secret);
    }
}
