package com.arittek.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")

public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String grade;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "student_enrolled",
            joinColumns = {
                    @JoinColumn(
                            name = "student_id", referencedColumnName = "id",
                            nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "subject_id", referencedColumnName = "id",
                            nullable = false, updatable = false)
            })
    private Set<Subject> subjects = new HashSet<>();

    public void addCourse(Subject subject) {
        this.subjects.add(subject);
        subject.getEnrolledStudents().add(this);

    }

    public void removeSubjects(Subject subject) {
        this.getSubjects().remove(subject);
        subject.getEnrolledStudents().remove(this);
    }

    public void removeSubjects() {
        for (Subject subject : new HashSet<>(subjects)) {
            removeSubjects(subject);
        }
    }


    public Student(String name, int age, String grade, Set<Subject> subjects) {

        this.name = name;
        this.age = age;
        this.grade = grade;
        this.subjects = subjects;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                '}';
    }


}
