package com.kelf.devops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class UserResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int rating; 

    @Column
    private String username;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private FeedbackQuestion question;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FeedbackQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestion question) {
        this.question = question;
    }
}
