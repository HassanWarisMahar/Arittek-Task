package com.arittek.demo.controller;

import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Student;
import com.arittek.demo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller

@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping(value = "/view")
    public String  getStudents(Model model) {

        List<Student> students = studentService.findAll();

        model.addAttribute("students",students);

        return "/student-list";

    }

    @PostMapping(value = "/create")
    List<Student> createStudent(@RequestBody Student student) throws BadResourceException, ResourceAlreadyExistsException {
         studentService.save(student);
         return  studentService.findAll();
    }

    @PutMapping(value = "update")
    String updateStudent(@RequestBody Student student) throws BadResourceException, ResourceNotFoundException {
        return studentService.update(student);
    }

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable(name = "id") Long studentId) throws ResourceNotFoundException {
        String message = studentService.deleteById(studentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
