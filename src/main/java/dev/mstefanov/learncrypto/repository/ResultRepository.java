package dev.mstefanov.learncrypto.repository;

import dev.mstefanov.learncrypto.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository  extends JpaRepository<Result, Long> {
}
