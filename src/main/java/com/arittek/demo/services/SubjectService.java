package com.arittek.demo.services;

import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Student;
import com.arittek.demo.model.Subject;
import com.arittek.demo.model.Teacher;
import com.arittek.demo.repository.StudentRepository;
import com.arittek.demo.repository.SubjectRepository;
import com.arittek.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    private boolean existsSubjectById(Long id) {
        return subjectRepository.existsById(id);
    }

    private boolean existsStudentById(Long id) {
        return subjectRepository.existsById(id);
    }

    private boolean existsTeacherById(Long id) {
        return subjectRepository.existsById(id);
    }

    public List<Subject> findAll() {

        return subjectRepository.findAll();
    }

    public Subject save(Subject subject) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(subject.getName())) {
            if (subject.getId() != null && existsSubjectById(subject.getId())) {
                throw new ResourceAlreadyExistsException("Subject with id: " + subject.getId() +
                        " already exists");
            } else
                return subjectRepository.save(subject);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save Student");
            exc.addErrorMessage("Subject is null or empty");
            throw exc;
        }

    }

    public String update(Subject subject)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(subject.getName())) {
            if (!existsSubjectById(subject.getId())) {
                //throw new ResourceNotFoundException("Cannot find Student with id: " + student.getId());
                return "Cannot find subject with id: " + subject.getId();
            } else
                subjectRepository.save(subject);
        } else {
            BadResourceException exc = new BadResourceException("Failed to update subject");
            exc.addErrorMessage("Subject is null or empty");
            throw exc;
        }
        return " Subject updated with id: " + subject.getId();
    }

    public String deleteById(Long id) throws ResourceNotFoundException {
        if (!existsSubjectById(id)) {
            //throw new ResourceNotFoundException("Cannot find student with id: " + id);
            return "Cannot find subject with id: " + id;
        } else {
            subjectRepository.deleteById(id);
            return " deleted successfully!";
        }
    }

    public Long count() {
        return subjectRepository.count();
    }

    public String assignStudentSubject(Long subjectId, Long studentId) {

        Subject subject = null;
        Student student = null;

        if (existsStudentById(studentId) && existsSubjectById(subjectId)) {
            try {
                subject = subjectRepository.findById(subjectId).get();
                student = studentRepository.findById(studentId).get();
                subject.enrolledStudents.add(student);
                subjectRepository.save(subject);
                return "Assigned Subject to Student Successfully";

            } catch (Exception e) {
                e.getMessage();
                return "Exception ";
            }

        } else {
            String message = "";
            if (!existsStudentById(studentId)) {
                message = " Student does not exists  with Id # " + studentId + " " + existsStudentById(studentId);
            }
            if (!existsSubjectById(subjectId)) {
                message += " \nSubject does not exists  with Id # " + subjectId + " " + existsSubjectById(subjectId);
            }
            return message;
        }

    }

    public String assignTeacherSubject(Long subjectId, Long teacherId) {
        Subject subject = null;
        Teacher teacher = null;
        if (existsSubjectById(subjectId) && existsSubjectById(teacherId) ) {
            try {
                subject = subjectRepository.findById(subjectId).get();
                teacher = teacherRepository.findById(teacherId).get();
                subject.setTeacher(teacher);
                subjectRepository.save(subject);
                return "Assigned Subject to Teacher Successfully";

            } catch (Exception e) {
                e.getMessage();
                return "Connection Error";
            }

        } else {
            String message = "";
            if(!existsSubjectById(subjectId)){

              message = "Specified Subject does not available with ID #" + subjectId+"\n";
            }
            if(!existsTeacherById(teacherId)){
                message += "Specified Teacher does not available with ID #" + teacherId;
            }
            return  message;
        }
    }
}
