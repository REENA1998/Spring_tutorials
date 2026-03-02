package com.example.schoolservice.controller;

import com.example.schoolservice.model.Student;
import com.example.schoolservice.repository.StudentRepository;
import com.example.schoolservice.service.StudentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exposes /students endpoint
 */
@RestController
public class SchoolController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentClient studentClient;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/school")
    public String fetchSchool() {
        return "My school";
    }

    @GetMapping("/fetch-students")
    public List<String> getStudentsFromSchool() {
        return studentClient.getStudents();
    }


}
