package com.example.learncrypto.service;

import com.example.learncrypto.model.Course;
import com.example.learncrypto.repository.CourseRepository;

import java.util.List;

public interface CourseService{

    List<Course> findAllCourses();

    Course findCourseById(Long id);

    Integer numberOfArticlesInCourse(Long id);

    void initCourses();
}
