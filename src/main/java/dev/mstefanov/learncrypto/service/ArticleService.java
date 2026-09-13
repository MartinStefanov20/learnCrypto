package dev.mstefanov.learncrypto.service;


import dev.mstefanov.learncrypto.model.Article;

import java.util.List;

public interface ArticleService {

    List<Article> findAllArticlesForCourse(Long courseId);

    List<Article> findAll();
}
