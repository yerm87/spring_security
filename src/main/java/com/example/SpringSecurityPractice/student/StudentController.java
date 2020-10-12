package com.example.SpringSecurityPractice.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static List<Student> students = Arrays.asList(
            new Student(1, "James"),
            new Student(2, "Maria"),
            new Student(3, "Tom")
    );

    @GetMapping(path="/{studentId}")
    public Student getStudent(@PathVariable(name="studentId") Integer studentId){
        return students.stream()
                        .filter(student -> studentId.equals(student.getStudentId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Student: " + studentId + " doesn't exist"));
    }
}
