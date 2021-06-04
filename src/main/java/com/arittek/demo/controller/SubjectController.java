package com.arittek.demo.controller;

import com.arittek.demo.services.SubjectService;
import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Subject;
import com.arittek.demo.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/subject")
public class SubjectController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SubjectService subjectService;

    @GetMapping(value = {"/view"})
    public String getSubjects(Model model) {

        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);

        System.out.println("Object printing " + subjects);
        return "subject/subject-list";

    }

    @GetMapping(value = "/view/{subejctId}")
    public String getSubejctById(Model model, @PathVariable long subjectId) {
        Subject subject = null;
        try {

            subject = subjectService.findById(subjectId);
            model.addAttribute("subject", subject);

        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Subject not found");
        }
        return "subject/subject";
    }

    @GetMapping(value = {"/create"})
    public String showAddSubject(Model model) {

        Subject subject = new Subject();

        model.addAttribute("add", true);
        model.addAttribute("subject", subject);

        return "subject/subject-edit";
    }

    @PostMapping(value = "/create")
    public String addSubject(Model model,
                             @ModelAttribute("subject") Subject subject) {
        try {

            Subject newSubject = subjectService.save(subject);
            return "redirect:/subject/view/" + String.valueOf(newSubject.getId());

        } catch (Exception ex) {
            //log exception first ,then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            //model.addAttribute("subject", subject)
            model.addAttribute("add", true);

        }
        return "subject/subject-edit";
    }

    @GetMapping(value = {"/{subjectId}/edit"})
    public String showEditSubject(Model model, @PathVariable long subjectId) {

        Subject subject = null;
        try {
            subject = subjectService.findById(subjectId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Subject not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("subject",subject);
        return "subject/subject-edit";
    }

    @PostMapping(value = {"/{subjectId}/edit"})
    public String updateSubject(Model model,
                                @PathVariable long subjectId,
                                @ModelAttribute("subject") Subject subject) {
        try {

            subject.setId(subjectId);
            subjectService.update(subject);
            return "redirect:/subject/view/" + String.valueOf(subject.getId());

        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "subject/subject-edit";
        }
    }

    @GetMapping(value = {"/{subejctId}/delete"})
    public String showDeleteSubejctById(
            Model model, @PathVariable long subejctId) {
        Subject subejct = null;
        try {
            subejct = subjectService.findById(subejctId);

            model.addAttribute("allowDelete", true);
            model.addAttribute("subejct", subejct);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "subejct not found ");
        }
        return "subejct/subejct";
    }

    @PostMapping(value = {"/{subejctId}/delete"})
    public String deletesubejctById(
            Model model, @PathVariable long subjectId) {
        try {

            subjectService.deleteById(subjectId);
            return "redirect:/subejct/view";

        } catch (ResourceNotFoundException ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "subejct/subejct";
        }
    }

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable(name = "id") Long studentId) throws ResourceNotFoundException {
        String message = subjectService.deleteById(studentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(value = "/{subjectId}/student/{studentId}")
    String addStudentToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long studentId
    ) {
        return subjectService.assignStudentSubject(subjectId, studentId);
    }


    @PutMapping(value = "/{subjectId}/teacher/{teacherId}")
    String assignTeacherToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId
    ) {
        return subjectService.assignTeacherSubject(subjectId, teacherId);
    }
}
