package com.arittek.demo.controller;

import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Developer;
import com.arittek.demo.model.Skill;
import com.arittek.demo.model.Student;
import com.arittek.demo.model.Subject;
import com.arittek.demo.repository.DeveloperRepository;
import com.arittek.demo.repository.SkillRepository;
import com.arittek.demo.services.StudentService;
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
@RequestMapping(value = "/student")
public class StudentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentService studentService;
    @Autowired
    SubjectService subjectService;

//
//    @RequestMapping("/view/{id}")
//    public String student(@PathVariable Long id, Model model) throws ResourceNotFoundException {
//
//        model.addAttribute("student", studentService.findById(id));
//        model.addAttribute("subjects", subjectService.findAll());
//        System.out.println("From get mapping all skill  " + id);
//        return "student/student";
//
//    }


    @GetMapping(value = {"/view"})
    public String getStudents(Model model) {

        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);

        System.out.println("Object printing " + students);
        return "student/student-list";

    }

    @GetMapping(value = "/view/{studentId}")
    public String getStudentById(Model model, @PathVariable long studentId) {
        Student student = null;
        List<Subject> subjects = null;

        try {

            student = studentService.findById(studentId);
            subjects = subjectService.findAll();
            model.addAttribute("student", student);
            model.addAttribute("subjects", subjects);
            return "student/student";


        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Student not found");
            return "student/student";
        }

    }

    @PostMapping(value = "/{studentId}/subjects")
    String addStudentToSubject(
            @RequestParam Long subjectId,
            @PathVariable Long studentId, Model model
    ) throws ResourceNotFoundException {

        System.out.print(studentId + " IDs " + subjectId);
        subjectService.assignStudentSubject(subjectId, studentId);

        try {
            model.addAttribute("student", studentService.findById(studentId));
            model.addAttribute("subject", subjectService.findAll());
            System.out.println("From try ");
            return "redirect:/student/view/" + studentId;

        } catch (ResourceNotFoundException e) {

            System.out.println("This works in the catch ");
            e.printStackTrace();
            model.addAttribute("students", studentService.findAll());
            return "student/student-list";
        }

    }

    @GetMapping(value = {"/create"})
    public String showAddStudent(Model model) {

        Student student = new Student();
        List<Subject> subjects = subjectService.findAll();

        model.addAttribute("add", true);
        model.addAttribute("student", student);
        model.addAttribute("subjectList", subjects);

        return "student/student-edit";
    }

    @PostMapping(value = "/create")
    public String addStudent(Model model,
                             @ModelAttribute("student") Student student) {
        try {

            Student newStudent = studentService.save(student);
            //subjectService.assignStudentSubject(subject.getId(), student.getId());

            return "redirect:/student/view/" + String.valueOf(newStudent.getId());

        } catch (Exception ex) {
            //log exception first ,then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            //model.addAttribute("student", student)
            model.addAttribute("add", true);

        }
        return "student/student-edit";
    }

    @GetMapping(value = {"/{studentId}/edit"})
    public String showEditStudent(Model model, @PathVariable long studentId) {

        Student student = null;
        try {
            student = studentService.findById(studentId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Student not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("student", student);
        return "student/student-edit";
    }

    @PostMapping(value = {"/{studentId}/edit"})
    public String updateStudent(Model model,
                                @PathVariable long studentId,
                                @ModelAttribute("student") Student student) {
        try {

            student.setId(studentId);
            studentService.update(student);
            return "redirect:/student/view/" + String.valueOf(student.getId());

        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "student/student-edit";
        }
    }

    @GetMapping(value = {"/{studentId}/delete"})
    public String showDeleteStudentById(
            Model model, @PathVariable long studentId) {
        Student student = null;
        try {
            student = studentService.findById(studentId);

            model.addAttribute("allowDelete", true);
            model.addAttribute("student", student);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Student not found ");
        }
        return "student/student";
    }

    @PostMapping(value = {"/{studentId}/delete"})
    public String deleteStudentById(
            Model model, @PathVariable long studentId) {
        try {

            studentService.deleteById(studentId);
            return "redirect:/student/view";

        } catch (ResourceNotFoundException ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "student/student";
        }
    }

}
