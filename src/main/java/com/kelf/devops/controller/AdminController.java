package com.kelf.devops.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kelf.devops.model.Admin;
import com.kelf.devops.model.BloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.Hospital;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.service.AdminService;
import com.kelf.devops.service.HospitalService;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "https://life4change.onrender.com")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private HospitalService hospitalservice;
    
    @PostMapping("/add")
    public String add(@RequestBody Admin a) {
    	return adminService.addadmin(a);
    }

    @GetMapping("/hospitals")
    public List<Hospital> getAllHospitals() {
        return adminService.viewAllHospitals();
    }

    @GetMapping("/bloodbanks")
    public List<BloodBank> getAllBloodBanks() {
        return adminService.viewAllBloodBanks();
    }

    @PutMapping("/hospital/block/{id}")
    public boolean blockHospital(@PathVariable int id) {
        return hospitalservice.blockHospital(id);
    }

    @DeleteMapping("/bloodbank/{id}")
    public String deleteBloodBank(@PathVariable int id) {
        return adminService.deleteBloodBank(id);
    }

    @PostMapping("/login")
    public Admin loginAdmin(@RequestBody Admin admin) {
        Admin existingAdmin = adminService.loginAdmin(admin.getUsername(), admin.getPassword());
        if (admin.getUsername().equals("admin")&&admin.getPassword().equals("admin")) {
            return existingAdmin;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
    @PostMapping("/addfeedback")
    public Feedback addfeedback(@RequestBody Feedback f) {
    	return adminService.addfeedback(f);
    }
    @GetMapping("/viewallfeedback")
    public List<Feedback> viewallfeedbacks() {
        return adminService.viewallfeedacks();
    }
   
    @GetMapping("/findallfeedbacks/{s}")
 	public List<Feedback> findallbyorg(@PathVariable String s){
 		return adminService.findallfeedbacks();
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
 	        adminService.addresponse(u, u.getQuestion().getId());
 	    }

 	    return ResponseEntity.ok("Responses submitted successfully");
 	}
    
    
}
