package com.example.learncrypto.service;


import com.example.learncrypto.model.Article;

import java.util.List;

public interface ArticleService {

    List<Article> findAllArticlesForCourse(Long courseId);

    List<Article> findAll();
}
