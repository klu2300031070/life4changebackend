package com.kelf.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelf.devops.model.FeedbackQuestion;

@Repository
public interface FeedbackQuestionRepo extends JpaRepository<FeedbackQuestion,Integer> {

}
