package com.example.learncrypto.service.impl;

import com.example.learncrypto.model.Article;
import com.example.learncrypto.model.Course;
import com.example.learncrypto.repository.ArticleRepository;
import com.example.learncrypto.repository.CourseRepository;
import com.example.learncrypto.service.ArticleService;
import com.example.learncrypto.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ArticleRepository articleRepository;

    public CourseServiceImpl(CourseRepository courseRepository, ArticleRepository articleRepository) {
        this.courseRepository = courseRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Course> findAllCourses() {
        return this.courseRepository.findAll();
    }

    @Override
    public Course findCourseById(Long id) {
        return this.courseRepository.findOneById(id);
    }

    @Override
    public Integer numberOfArticlesInCourse(Long id) {
        Course course = this.findCourseById(id);
        return course.getArticles().size();
    }

    @Override
    public void initCourses(){
        if(this.courseRepository.count() == 0){
            Course course1 = new Course();
            course1.setTitle("Crypto Basics");
            course1.setDescription("What is crypto & why is it important?\n" +
                    "How does crypto work?\n" +
                    "What is a blockchain?");

            Article article1Course1 = new Article();
            article1Course1.setName("What is cryptocurrency?");
            article1Course1.setDescription("Learn what cryptocurrency is, what you can do with it & why it has value. Learn about Bitcoin & sound money.");

            Article article2Course1 = new Article();
            article2Course1.setName("Bitcoin, scarcity & trust in money");
            article2Course1.setDescription("Learn why scarcity and trust are crucial to money having value; discover why modern money isn’t scarce and how Bitcoin is.");

            this.articleRepository.saveAll(List.of(article1Course1, article2Course1));

            course1.setArticles(List.of(article1Course1, article2Course1));

            this.courseRepository.save(course1);
        }

    }
}
