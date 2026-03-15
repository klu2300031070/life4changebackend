package com.kelf.devops.service;

import java.util.List;
import com.kelf.devops.model.Hospital;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.model.BloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.FeedbackQuestion;
import com.kelf.devops.model.Admin;

public interface AdminService {
    List<Hospital> viewAllHospitals();
    List<BloodBank> viewAllBloodBanks();
    String deleteHospital(int id);
    String deleteBloodBank(int id);
    String addadmin(Admin a);
    Feedback addfeedback(Feedback f);
    List<Feedback> viewallfeedacks();
    Admin loginAdmin(String username, String password);
	List<Feedback> findallfeedbacks();
	void addresponse(UserResponse u, int qid);
}
