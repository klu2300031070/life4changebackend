package com.kelf.devops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "contributed_bloodbank")
public class ContributedBloodBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "given_units")
    private int givenUnits;

    @Column(name = "blood_bank")
    private String bloodBank;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonBackReference // ✅ prevents infinite recursion
    private RequestBlood request;

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getGivenUnits() {
        return givenUnits;
    }
    public void setGivenUnits(int givenUnits) {
        this.givenUnits = givenUnits;
    }

    public String getBloodBank() {
        return bloodBank;
    }
    public void setBloodBank(String bloodBank) {
        this.bloodBank = bloodBank;
    }

    public RequestBlood getRequest() {
        return request;
    }
    public void setRequest(RequestBlood request) {
        this.request = request;
    }
}
