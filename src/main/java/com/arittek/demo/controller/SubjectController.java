package com.arittek.demo.controller;

import com.arittek.demo.model.Teacher;
import com.arittek.demo.services.SubjectService;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Subject;
import com.arittek.demo.services.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    TeacherService teacherService;

    @GetMapping(value = {"/view"})
    public String getSubjects(Model model) {

        List<Subject> subject = subjectService.findAll();
        model.addAttribute("subjects", subject);

        System.out.println("Object printing " + subject);
        return "subject/subject-list";

    }

    @GetMapping(value = "/view/{subjectId}")
    public String getSubjectById(Model model, @PathVariable long subjectId) {

        Subject subject = null;
        List<Teacher> teacher = null;
        try {

            subject = subjectService.findById(subjectId);
            teacher = teacherService.findAll();

            model.addAttribute("subject", subject);
            model.addAttribute("teachers", teacher);

            return "subject/subject";

        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Subject not found");
            return "subject/subject-list";
        }

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

    @PostMapping(value = "/{subjectId}/teacher")
    String assignTeacherToSubject(
            @RequestParam Long teacherId,
            @PathVariable Long subjectId, Model model
    ) {

        subjectService.assignTeacherSubject(subjectId, teacherId);

        try {

            model.addAttribute("teachers", teacherService.findAll());
            model.addAttribute("subject", subjectService.findById(subjectId));
            return "redirect:/subject/view/" + subjectId;

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            model.addAttribute("subjects", subjectService.findAll());
            return "subject/subject-list";
        }
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
        model.addAttribute("subject", subject);
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

    @GetMapping(value = {"/{subjectId}/delete"})
    public String showDeleteSubjectById(
            Model model, @PathVariable long subjectId) {
        Subject subject = null;
        try {
            subject = subjectService.findById(subjectId);

            model.addAttribute("allowDelete", true);
            model.addAttribute("subject", subject);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "subject not found ");
        }
        return "subject/subject";
    }

    @PostMapping(value = {"/{subjectId}/delete"})
    public String deleteSubjectById(
            Model model, @PathVariable long subjectId) {
        try {

            subjectService.deleteById(subjectId);
            return "redirect:/subject/view";

        } catch (ResourceNotFoundException ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "subject/subject";
        }
    }
