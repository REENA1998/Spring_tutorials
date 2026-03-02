package com.example.schoolservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.example.schoolservice.model.Student;

@FeignClient(name = "school-service", url = "http://localhost:8081")
public interface StudentClient {
    @GetMapping("/students")
    List<Student> getStudents();
}
