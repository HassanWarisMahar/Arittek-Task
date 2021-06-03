package com.arittek.demo.repository;

import com.arittek.demo.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameContaining(String name);

    Optional<Student> findById(Long studentId);
}
