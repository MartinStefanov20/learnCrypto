package com.example.learncrypto.repository;

import com.example.learncrypto.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllById(Long id);

    Quiz findOneById (Long id);

    Quiz findOneByUserUsername(String name);
}
