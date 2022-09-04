package com.example.learncrypto.service.impl;

import com.example.learncrypto.model.Article;
import com.example.learncrypto.model.Course;
import com.example.learncrypto.repository.ArticleRepository;
import com.example.learncrypto.repository.CourseRepository;
import com.example.learncrypto.service.ArticleService;
import com.example.learncrypto.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    public ArticleServiceImpl(ArticleRepository articleRepository, CourseRepository courseRepository, CourseService courseService) {
        this.articleRepository = articleRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    @Override
    public List<Article> findAllArticlesForCourse(Long courseId) {
        Course course = this.courseService.findCourseById(courseId);
        if(course != null){
            return course.getArticles();
        }
        return null;
    }

    @Override
    public List<Article> findAll() {
        return (List<Article>) articleRepository.findAll();
    }
}
