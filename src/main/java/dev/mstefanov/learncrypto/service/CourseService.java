package dev.mstefanov.learncrypto.service;

import dev.mstefanov.learncrypto.model.Course;
import dev.mstefanov.learncrypto.repository.CourseRepository;

import java.util.List;

public interface CourseService{

    List<Course> findAllCourses();

    Course findCourseById(Long id);

    Integer numberOfArticlesInCourse(Long id);

    void initCourses();
}
