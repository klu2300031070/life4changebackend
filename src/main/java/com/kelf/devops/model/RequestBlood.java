package com.kelf.devops.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "request_blood")
public class RequestBlood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @Column(name = "units_needed", nullable = false)
    private int unitsNeeded;

    @Column(name = "urgency", nullable = false)
    private String urgency;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private String date;

    @Column(name = "accepted_org")
    private String acceptedOrg;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "patient_age", nullable = false)
    private int patientAge;

    @Column(name = "patient_info")
    private String patientInfo;

    @Column(name = "hospital_username", nullable = false)
    private String hospitalUsername;

    @Column(name = "accepted_units")
    private int acceptedUnits;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // ✅ prevents infinite recursion
    private List<ContributedBloodBank> bloodbanks = new ArrayList<>();

    // 🔹 Convenience method to maintain both sides of the relationship
    public void addBloodBank(ContributedBloodBank bank) {
        bloodbanks.add(bank);
        bank.setRequest(this);
    }

    public void removeBloodBank(ContributedBloodBank bank) {
        bloodbanks.remove(bank);
        bank.setRequest(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getUnitsNeeded() {
        return unitsNeeded;
    }
    public void setUnitsNeeded(int unitsNeeded) {
        this.unitsNeeded = unitsNeeded;
    }

    public String getUrgency() {
        return urgency;
    }
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getAcceptedOrg() {
        return acceptedOrg;
    }
    public void setAcceptedOrg(String acceptedOrg) {
        this.acceptedOrg = acceptedOrg;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getPatientAge() {
        return patientAge;
    }
    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientInfo() {
        return patientInfo;
    }
    public void setPatientInfo(String patientInfo) {
        this.patientInfo = patientInfo;
    }

    public String getHospitalUsername() {
        return hospitalUsername;
    }
    public void setHospitalUsername(String hospitalUsername) {
        this.hospitalUsername = hospitalUsername;
    }

    public int getAcceptedUnits() {
        return acceptedUnits;
    }
    public void setAcceptedUnits(int acceptedUnits) {
        this.acceptedUnits = acceptedUnits;
    }

    public List<ContributedBloodBank> getBloodbanks() {
        return bloodbanks;
    }
    public void setBloodbanks(List<ContributedBloodBank> bloodbanks) {
        this.bloodbanks = bloodbanks;
    }
}
