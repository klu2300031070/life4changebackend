package com.kelf.devops.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelf.devops.model.BloodBank;
import com.kelf.devops.model.BloodData;
import com.kelf.devops.model.BloodDonor;
import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.RequestBlood;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.repository.BloodBankRepository;
import com.kelf.devops.repository.BloodDataRepository;
import com.kelf.devops.repository.BloodDonorRepository;
import com.kelf.devops.repository.ContributedBBREPO;
import com.kelf.devops.repository.RequestBlooodRepisotory;
import com.kelf.devops.service.BloodBankServiceimpl;

@RestController
@RequestMapping("/bloodbankapi")
@CrossOrigin(origins="*")
public class BloodBankController {
	
	@Autowired
	private BloodDonorRepository br;
	
	@Autowired
	private BloodDataRepository bdr;
	
	@Autowired
	private BloodBankServiceimpl bbs;
	
	@Autowired
	private RequestBlooodRepisotory rbr;
	
	@Autowired
	private BloodBankRepository bbr;
	
	@Autowired
	private ContributedBBREPO cbr;
	
	
	
	@PostMapping("/registerbloodbank")
	public ResponseEntity<String> registerbloodbank(@RequestBody BloodBank bb) {
	    String s = bbs.addbloodbank(bb);
	    
	    if (s.contains("successfully")) {
	        return ResponseEntity.ok(s);  
	    } else {
	        return ResponseEntity.status(500).build(); 
	    }
	}
	
	@PostMapping("/registerblooddonor")
    public ResponseEntity<String> registerbloodDonor(@RequestBody BloodDonor b) {
    	 try
  	   {
  		 String s=bbs.addblooddonor(b);
  		  return ResponseEntity.ok(s); 
  	   }
  	   catch(Exception e)
  	   {
  		  
  		   return ResponseEntity.status(500).body("BloodDonor Registration Failed... ");
  	   }
    }
	@PostMapping("/registerblooddonors")
	public ResponseEntity<String> registerMultipleBloodDonors(@RequestBody List<BloodDonor> donors) {
	    try {
	        for (BloodDonor donor : donors) {
	            bbs.addblooddonor(donor); 
	        }
	        return ResponseEntity.ok("All Blood Donors Added Successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Bulk Registration Failed...");
	    }
	}
	
	@GetMapping("/viewallblooddonors")
    public ResponseEntity<List<BloodDonor>> getblooddonors() {
 	   try {
	            List<BloodDonor> requests = br.findAll();
	            return ResponseEntity.ok(requests);
	        } catch (Exception e) {
	             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
 	   
    }
    
    @GetMapping("/viewallbyblood")
    public ResponseEntity<List<BloodDonor>> getAllByBlood(
            @RequestParam("bloodType") String bloodType) {
        try {
            List<BloodDonor> requests = br.findByBloodType(bloodType);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
     	   return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	 @GetMapping("/viewallbloodbanks")
	 public ResponseEntity<List<BloodBank>> getAllbloodbanks() {
	        try {
	            List<BloodBank> requests = bbs.viewall();
	            return ResponseEntity.ok(requests);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).build();
	        }
	    }
	 @PostMapping("/checkbloodbanklogin")
	 public ResponseEntity<?> checkvoterlogin(@RequestBody BloodBank bb) {
		    try {
		        BloodBank b=bbs.findbloodbank(bb.getUsername(),bb.getPassword());

		        if (b != null) {
		            return ResponseEntity.ok(b); 
		        } else {
		            return ResponseEntity.status(401).body("Invalid username or password");
		        }

		    } catch (Exception e) {
		        return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
		    }
		}
	 @PostMapping("/registerblooddata")
	 public ResponseEntity<String> registerblooddata(@RequestBody BloodData  bd) {
	    	 try
	  	   {
	  		 bdr.save(bd);
	  		  return ResponseEntity.ok("Registred Successfully"); 
	  	   }
	  	   catch(Exception e)
	  	   {
	  		  
	  		   return ResponseEntity.status(500).body("BloodBank Registration Failed... ");
	  	   }
	    }
	 @GetMapping("/viewallblooddata")
	 public ResponseEntity<List<BloodData>> getallblooddata(){
		 List<BloodData> list=bbs.viewalldata();
		 try{
	  		
	  		  return ResponseEntity.ok( list);
	  	   }
	  	   catch(Exception e)
	  	   {
	  		  
	  		   return ResponseEntity.status(500).build();
	  	   }
		 
	 }
	 @PutMapping("/decrement/{type}")
	 public ResponseEntity<BloodData> decrementBloodUnit(@PathVariable String type) {
	 	try {
	 		BloodData bloodData = bdr.findByType(type);

	 		if (bloodData == null) {
	 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	 		}

	 		if (bloodData.getAunits() <= 0) {
	 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	 		}

	 		bloodData.setAunits(bloodData.getAunits() - 1);
	 		bloodData.setUsedunits(bloodData.getUsedunits() + 1);

	 		bdr.save(bloodData);

	 		return ResponseEntity.ok(bloodData);

	 	} catch (Exception e) {
	 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	 	}
	 }
	 
	 @PostMapping("/addbloodrequest")
	 public ResponseEntity<String> addbloodrequest(@RequestBody RequestBlood r){
		 try
	  	   {
	  		  String s=bbs.addrequest(r);
	  		  return ResponseEntity.ok(s);
	  	   }
	  	   catch(Exception e)
	  	   {
	  		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  	   }
	 }
	 @PutMapping("/updatebloodstatus")
	 public ResponseEntity<String> updateBloodStatus(@RequestBody ContributedBloodBank contribution) {
	     try {
	         RequestBlood existingRequest = contribution.getRequest();

	         if (existingRequest == null || existingRequest.getId() == null) {
	             return ResponseEntity.badRequest().body("Request ID is required.");
	         }

	         int unitsToAccept = contribution.getGivenUnits();
	         String acceptingOrg = contribution.getBloodBank();

	         // Fetch the existing request from DB
	         RequestBlood requestFromDb = rbr.findById(existingRequest.getId()).orElse(null);
	         if (requestFromDb == null) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found.");
	         }

	         // Fetch blood data for this org & blood type
	         BloodData bloodData = bdr.findByOrgAndType(acceptingOrg, requestFromDb.getBloodGroup());
	         if (bloodData == null) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body("No blood data found for type: " + requestFromDb.getBloodGroup()
	                     + " and org: " + acceptingOrg);
	         }

	         if (bloodData.getAunits() < unitsToAccept) {
	             return ResponseEntity.badRequest()
	                 .body("Insufficient available units for type: " + requestFromDb.getBloodGroup()
	                     + " in org: " + acceptingOrg);
	         }

	         // Update BloodData
	         bloodData.setAunits(bloodData.getAunits() - unitsToAccept);
	         bloodData.setUsedunits(bloodData.getUsedunits() + unitsToAccept);
	         bdr.save(bloodData);

	         // Update RequestBlood
	         //requestFromDb.setAcceptedOrg(acceptingOrg);
	         requestFromDb.setUnitsNeeded(requestFromDb.getUnitsNeeded() - unitsToAccept);
	         requestFromDb.setAcceptedUnits(requestFromDb.getAcceptedUnits() + unitsToAccept);
	         requestFromDb.setStatus(requestFromDb.getUnitsNeeded() <= 0 ? "ACCEPTED" : "PARTIALLY_ACCEPTED");

	         // Add the contribution to bloodbanks list
	         contribution.setRequest(requestFromDb);
	         requestFromDb.getBloodbanks().add(contribution);

	         rbr.save(requestFromDb);

	         return ResponseEntity.ok("Request updated successfully with " + unitsToAccept + " units from " + acceptingOrg);

	     } catch (Exception e) {
	         return ResponseEntity.status(500).body("Server error: " + e.getMessage());
	     }
	 }


 @GetMapping("/viewallbloodrequests")
	    public ResponseEntity<List<RequestBlood>> getAllbloodRequests() {
	        try {
	            List<RequestBlood> requests = rbr.findByStatusIgnoreCase("pending");
	            return ResponseEntity.ok(requests);
	        } catch (Exception e) {
	        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
 	 
 @GetMapping("/viewallrequests")
 public ResponseEntity<List<RequestBlood>> getAllrequests() {
     try {
         List<RequestBlood> requests = rbr.findAll();

         for (RequestBlood request : requests) {
             // Fetch all contributions for this request
             List<ContributedBloodBank> bloodBanks = cbr.findByRequest(request);

             // Merge contributions by blood bank name
             Map<String, ContributedBloodBank> mergedMap = new HashMap<>();

             for (ContributedBloodBank contribution : bloodBanks) {
                 String bankName = contribution.getBloodBank();

                 if (mergedMap.containsKey(bankName)) {
                     // Sum up given units if same blood bank already exists
                     ContributedBloodBank existing = mergedMap.get(bankName);
                     existing.setGivenUnits(existing.getGivenUnits() + contribution.getGivenUnits());
                 } else {
                     // Add new entry to map
                     mergedMap.put(bankName, contribution);
                 }
             }

             // Replace with merged list
             request.setBloodbanks(List.copyOf(mergedMap.values()));
         }

         return ResponseEntity.ok(requests);

     } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
 }


 	 @PutMapping("/updatestatus/{id}")
 	 public String updatestatus(@PathVariable int id ) {
	            return bbs.updatestatus(id);
	       
	    }
 	 @GetMapping("/findemail/{email}")
 	 public boolean checkemail(@PathVariable String email) {
 	 	return (bbs.findemail(email)!=null) ;
 	 	
 	 }
 	 @GetMapping("/findphone/{phone}")
 	 public boolean checkphone(@PathVariable String phone) {
 	 	return bbs.findphone(phone)!=null;
	 }

 	@GetMapping("/findusername/{username}")
	 public boolean checkusername(@PathVariable String username) {
	 	return bbs.findusername(username)!=null;
	 }
 	@GetMapping("/findname/{name}")
	 public boolean checkname(@PathVariable String name) {
	 	return bbs.findusername(name)!=null;
	 }
 	
 	@GetMapping("/findallfeedbacks")
 	public List<Feedback> findallbyorg(){
 		return bbs.findallfeedbacks();
 	}
 	
 	@PostMapping("/submitresponses")
 	public ResponseEntity<String> submitResponses(@RequestBody List<UserResponse> responses) {
 	    if (responses == null || responses.isEmpty()) {
 	        return ResponseEntity.badRequest().body("No responses provided");
 	    }

 	    for (UserResponse u : responses) {
 	        if (u.getQuestion() == null || u.getQuestion().getId() == 0) {
 	            return ResponseEntity.badRequest().body("Each response must include a valid question ID");
 	        }
 	        bbs.addresponse(u, u.getQuestion().getId());
 	    }

 	    return ResponseEntity.ok("Responses submitted successfully");
 	}


 	 
}
