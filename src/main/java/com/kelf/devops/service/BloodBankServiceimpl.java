package com.kelf.devops.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kelf.devops.model.BloodBank;
import com.kelf.devops.model.BloodData;
import com.kelf.devops.model.BloodDonor;
import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.FeedbackQuestion;
import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.RequestBlood;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.repository.BloodBankRepository;
import com.kelf.devops.repository.BloodDataRepository;
import com.kelf.devops.repository.BloodDonorRepository;
import com.kelf.devops.repository.FeedbackQuestionRepo;
import com.kelf.devops.repository.FeedbackRepo;
import com.kelf.devops.repository.RequestBlooodRepisotory;

@Service
public class BloodBankServiceimpl implements BloodBankService {

	@Autowired
	private BloodBankRepository bbr;
	
	@Autowired
	private BloodDataRepository bdr;
	
	@Autowired
    private BloodDonorRepository br;
	
	@Autowired
	private RequestBlooodRepisotory rbr;
	
	@Autowired
    private FeedbackRepo fr;
	
	@Autowired
	public FeedbackQuestionRepo fqr;

	@Override
	public String addbloodbank(BloodBank bb) {
		  try {
		        
		        bbr.save(bb);

		       
		        List<String> bloodTypes = List.of("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-");

		        
		        for (String type : bloodTypes) {
		            BloodData bd = new BloodData();
		            bd.setType(type);
		            bd.setOrg(bb.getName());
		            bd.setAunits(0);
		            bd.setUsedunits(0);
		            bd.setDonatedunits(0);
		            bdr.save(bd);
		        }

		        return "BloodBank and default blood data registered successfully."; 
		    } catch(Exception e) {
		        e.printStackTrace(); 
		        return "BloodBank Registration Failed... ";
		    }
		  

	}

	@Override
	public List<BloodBank> viewall() {

		return bbr.findAll();
	}
	
	public BloodBank findbloodbank(String a,String b) {
		return bbr.findByUsernameAndPassword(a,b);
	}
	
	public List<BloodData> viewalldata(){
		return bdr.findAll();
	}

	@Override
	public String addblooddonor(BloodDonor b) {
		 br.save(b);
  		 
	  	BloodData bd = bdr.findByOrgAndType(b.getOrg(), b.getBloodType());
	  	bd.setDonatedunits(bd.getDonatedunits() + 1);
	      bd.setAunits(bd.getAunits() + 1);
	      bdr.save(bd);
	      
	      return "Registred Successfully";
	}
	
	public String addrequest(RequestBlood rb) {
		
		rbr.save(rb);
		return "Request added Successfully";
		
	}
	public String Updatebloodstatus(RequestBlood r) {
		RequestBlood rb=rbr.findById(r.getId()).orElse(null);
		 if (rb == null) {
             return "No blood data found for type: " 
                 + rb.getBloodGroup() + " and org: " + rb.getAcceptedOrg();
         }
		 
		 BloodData bloodData = bdr.findByOrgAndType(rb.getAcceptedOrg(),rb.getBloodGroup());
		 
         if (bloodData.getAunits() <= 0) {
             return "Insufficient available units for type: " 
                 + rb.getBloodGroup() + " in org: " + rb.getAcceptedOrg();
         }
         
      
         bloodData.setAunits(bloodData.getAunits() - 1);
         bloodData.setUsedunits(bloodData.getUsedunits() + 1);
         bdr.save(bloodData);

        
         rb.setStatus(r.getStatus());
         rb.setAcceptedOrg(r.getAcceptedOrg());
         rbr.save(rb);
          return "Request accepted and blood stock updated.";
		
	}

	@Override
	public String updatestatus(int id) {
		BloodBank bb=bbr.findById(id).orElse(null);
		if(bb==null) {
			return "NOT FOUND";
		}
		bb.setStatus(!(bb.isStatus()));
		bbr.save(bb);
		return "Updated Status";
	}

	@Override
	public BloodBank findemail(String e) {
		return bbr.findByEmail(e);
	}

	@Override
	public BloodBank findphone(String e) {
		
		return bbr.findByPhone(e);
	}

	@Override
	public BloodBank findusername(String u) {
		
		return bbr.findByUsername(u);
	}

	@Override
	public BloodBank findname(String n) {
		
		return bbr.findByName(n);
	}
	
	
    public String updateBloodStatus(RequestBlood updatedRequest) {
        RequestBlood existingRequest = rbr.findById(updatedRequest.getId()).orElse(null);
        if (existingRequest == null) return "Request not found.";

        int unitsToAccept = updatedRequest.getAcceptedUnits();
        String acceptingOrg = updatedRequest.getAcceptedOrg();

        BloodData bloodData = bdr.findByOrgAndType(acceptingOrg, existingRequest.getBloodGroup());
        if (bloodData == null) return "Blood data not found.";

        if (bloodData.getAunits() < unitsToAccept) return "Insufficient units.";

        // Update BloodData
        bloodData.setAunits(bloodData.getAunits() - unitsToAccept);
        bloodData.setUsedunits(bloodData.getUsedunits() + unitsToAccept);
        bdr.save(bloodData);

        // Update RequestBlood
        existingRequest.setAcceptedOrg(acceptingOrg);
        existingRequest.setUnitsNeeded(existingRequest.getUnitsNeeded() - unitsToAccept);
        existingRequest.setAcceptedUnits(existingRequest.getAcceptedUnits() + unitsToAccept);
        existingRequest.setStatus(existingRequest.getUnitsNeeded() <= 0 ? "ACCEPTED" : "Pending");

        ContributedBloodBank contributed = new ContributedBloodBank();
        contributed.setBloodBank(acceptingOrg);
        contributed.setGivenUnits(unitsToAccept);
        contributed.setRequest(existingRequest);
        existingRequest.getBloodbanks().add(contributed);

        rbr.save(existingRequest);
        return "Request updated successfully with " + unitsToAccept + " units from " + acceptingOrg;
    }

	@Override
	public List<Feedback> findallfeedbacks() {
		return fr.findByOrg("blood bank");
	}

	@Override
	public void addresponse(UserResponse u, int qid) {
	    FeedbackQuestion fq = fqr.findById(qid).orElse(null);
	    if (fq != null) {
	        u.setQuestion(fq);

	        // Initialize list if null
	        if (fq.getUsers() == null) {
	            fq.setUsers(new ArrayList<>());
	        }

	        fq.getUsers().add(u);
	        fqr.save(fq);
	    } else {
	        throw new RuntimeException("Question not found with id: " + qid);
	    }
	}

	
	
	

}
