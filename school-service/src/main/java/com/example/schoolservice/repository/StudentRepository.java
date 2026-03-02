package com.example.schoolservice.repository;

import com.example.schoolservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // you can add custom queries here later
}
