package com.arittek.demo.controller;

import com.arittek.demo.services.SubjectService;
import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Subject;
import com.arittek.demo.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    List<Subject> getSubjects() {
        return subjectService.findAll();
    }

    @PostMapping
    Subject createSubject(@RequestBody Subject subject) throws BadResourceException, ResourceAlreadyExistsException {
//         subjectService.save(subject);
         return subjectService.save(subject);
    }

    @PutMapping
    String updateSubject(@RequestBody Subject subject) throws BadResourceException, ResourceNotFoundException {

        return subjectService.update(subject);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable(name = "id") Long studentId) throws ResourceNotFoundException {
        String message = subjectService.deleteById(studentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(value = "/{subjectId}/student/{studentId}")
    String addStudentToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long studentId
    ) {
        return subjectService.assignStudentSubject(subjectId,studentId);
    }


    @PutMapping(value = "/{subjectId}/teacher/{teacherId}")
    String assignTeacherToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId
    ) {
        return subjectService.assignTeacherSubject(subjectId,teacherId);
    }
}
