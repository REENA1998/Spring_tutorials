package com.example.schoolservice.config;

import com.example.schoolservice.model.Student;
import com.example.schoolservice.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pre-load some students into H2 on startup.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(StudentRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Student("Alice Johnson", "A"));
                repo.save(new Student("Bob Smith", "B"));
                repo.save(new Student("Charlie Lee", "A"));
            }
        };
    }
}
