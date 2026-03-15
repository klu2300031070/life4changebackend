package com.kelf.devops.service;

import java.util.List;

import com.kelf.devops.model.BloodBank;
import com.kelf.devops.model.BloodData;
import com.kelf.devops.model.BloodDonor;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.RequestBlood;
import com.kelf.devops.model.UserResponse;

public interface BloodBankService {
	public String addbloodbank(BloodBank bb);
	public List<BloodBank> viewall();
	public BloodBank findbloodbank(String a,String b);
	public List<BloodData> viewalldata();
	public String addblooddonor(BloodDonor b);
	public String Updatebloodstatus(RequestBlood r);
	public String updatestatus(int id);
	public BloodBank findemail(String e);
	public BloodBank findphone(String e);
	public BloodBank findusername(String u);
	public BloodBank findname(String n);
	public List<Feedback> findallfeedbacks();
	public void addresponse(UserResponse u,int qid);

}
