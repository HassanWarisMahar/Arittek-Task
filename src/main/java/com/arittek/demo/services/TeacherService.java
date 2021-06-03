package com.arittek.demo.services;


import com.arittek.demo.exceptions.BadResourceException;
import com.arittek.demo.exceptions.ResourceAlreadyExistsException;
import com.arittek.demo.exceptions.ResourceNotFoundException;
import com.arittek.demo.model.Teacher;
import com.arittek.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    private boolean existsById(Long id) {
        return teacherRepository.existsById(id);
    }

    public Teacher findById(Long id) throws ResourceNotFoundException {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            throw new ResourceNotFoundException("Cannot find Teacher with id: " + id);
        } else return teacher;
    }

    public Teacher save(Teacher teacher) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(teacher.getName())) {
            if (teacher.getId() != null && existsById(teacher.getId())) {
                throw new ResourceAlreadyExistsException("Teacher with id: " +teacher.getId() +
                        " already exists");
            }
            return teacherRepository.save(teacher);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save teacher");
            exc.addErrorMessage("Teacher is null or empty");
            throw exc;
        }
    }

    public Teacher update(Teacher teacher)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(teacher.getName())) {
            if (!existsById(teacher.getId())) {
                throw new ResourceNotFoundException("Cannot find teacher with id: " +teacher.getId());
            }
            teacherRepository.save(teacher);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save teacher");
            exc.addErrorMessage("Teacher is null or empty");
            throw exc;
        }
        return teacher;
    }

    public String deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            //throw new ResourceNotFoundException("Cannot find teacher with id: " + id);
            return  "Cannot find teacher with id: " + id;
        } else {
            teacherRepository.deleteById(id);
            return "Deleted successfully";
        }

    }

    public Long count() {
        return teacherRepository.count();
    }

    public List<Teacher> findAll() {

       return teacherRepository.findAll();
    }
}
