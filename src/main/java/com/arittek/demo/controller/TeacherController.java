package com.arittek.demo.controller;


import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Student;
import com.arittek.demo.model.Teacher;
import com.arittek.demo.model.Teacher;
import com.arittek.demo.services.TeacherService;
import com.arittek.demo.services.TeacherService;
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
@RequestMapping("/teacher")
public class TeacherController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TeacherService teacherService;

    @GetMapping(value = {"/view"})
    public String getTeachers(Model model) {

        List<Teacher> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);

        System.out.println("Object printing " + teachers);
        return "teacher/teacher-list";

    }

    @GetMapping(value = "/view/{teacherId}")
    public String getTeacherById(Model model, @PathVariable long teacherId) {
        Teacher teacher = null;
        try {

            teacher = teacherService.findById(teacherId);
            model.addAttribute("teacher", teacher);

        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Teacher not found");
        }
        return "teacher/teacher";
    }

    @GetMapping(value = {"/create"})
    public String showAddTeacher(Model model) {

        Teacher teacher = new Teacher();

        model.addAttribute("add", true);
        model.addAttribute("teacher", teacher);

        return "teacher/teacher-edit";
    }

    @PostMapping(value = "/create")
    public String addTeacher(Model model,
                             @ModelAttribute("teacher") Teacher teacher) {
        try {

            Teacher newTeacher = teacherService.save(teacher);
            return "redirect:/teacher/view/" + String.valueOf(newTeacher.getId());

        } catch (Exception ex) {
            //log exception first ,then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            //model.addAttribute("Teacher", Teacher)
            model.addAttribute("add", true);

        }
        return "teacher/teacher-edit";
    }



    @GetMapping(value = {"/{teacherId}/edit"})
    public String showEditTeacher(Model model, @PathVariable long teacherId) {

        Teacher teacher = null;
        try {
            teacher = teacherService.findById(teacherId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "teacher not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("teacher", teacher);
        return "teacher/teacher-edit";
    }

    @PostMapping(value = {"/{teacherId}/edit"})
    public String updateTeacher(Model model,
                                @PathVariable long teacherId,
                                @ModelAttribute("teacher") Teacher teacher) {
        try {

            teacher.setId(teacherId);
            teacherService.update(teacher);
            return "redirect:/teacher/view/" + String.valueOf(teacher.getId());

        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "teacher/teacher-edit";
        }
    }

    @GetMapping(value = {"/{teacherId}/delete"})
    public String showDeleteTeacherById(
            Model model, @PathVariable long teacherId) {
        Teacher teacher = null;
        try {
            teacher = teacherService.findById(teacherId);

            model.addAttribute("allowDelete", true);
            model.addAttribute("teacher", teacher);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "teacher not found ");
        }
        return "teacher/teacher";
    }

    @PostMapping(value = {"/{teacherId}/delete"})
    public String deleteTeacherById(
            Model model, @PathVariable long teacherId) {
        try {

            teacherService.deleteById(teacherId);
            return "redirect:/teacher/view";

        } catch (ResourceNotFoundException ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "teacher/teacher";
        }
    }

}
