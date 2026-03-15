package com.kelf.devops.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
    
    @Column
    private String org;
    
    @Column
    private String startdate;
    
    @Column
    private String enddate;

	@OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FeedbackQuestion> feedbackset;

	 public String getOrg() {
			return org;
		}

		public void setOrg(String org) {
			this.org = org;
		}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FeedbackQuestion> getFeedbackset() {
        return feedbackset;
    }

    public void setFeedbackset(List<FeedbackQuestion> feedbackset) {
        this.feedbackset = feedbackset;
    }

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	
}
