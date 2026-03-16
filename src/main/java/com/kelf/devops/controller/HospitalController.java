package com.kelf.devops.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kelf.devops.model.BloodData;
import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.Hospital;
import com.kelf.devops.model.RequestBlood;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.service.HospitalService;

@RestController
@RequestMapping("/hospitalapi")
@CrossOrigin(origins = "https://life4change.onrender.com")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    // ✅ Base API Test
    @GetMapping("/")
    public String home() {
        return "Hospital Registration API is running!";
    }

    // ✅ Register hospital
    @PostMapping("/register")
    public ResponseEntity<?> registerHospital(@RequestBody Hospital hospital) {

        if (hospitalService.getHospitalByUsername(hospital.getUsername()) != null) {
            return new ResponseEntity<>("Username already taken.", HttpStatus.BAD_REQUEST);
        }

        if (hospitalService.getHospitalByEmail(hospital.getEmail()) != null) {
            return new ResponseEntity<>("Email already registered.", HttpStatus.BAD_REQUEST);
        }

        Hospital savedHospital = hospitalService.registerHospital(hospital);
        return new ResponseEntity<>(savedHospital, HttpStatus.CREATED);
    }

    // ✅ Login hospital
    @PostMapping("/login")
    public ResponseEntity<?> loginHospital(@RequestBody Hospital hospital) {
        Hospital existingHospital = hospitalService.loginHospital(
                hospital.getUsername(),
                hospital.getPassword()
        );

        if (existingHospital == null) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        if (existingHospital.isBlocked()) {  // ✅ check blocked status
            return new ResponseEntity<>("Account blocked", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(existingHospital, HttpStatus.OK);
    }

    // ✅ Get hospital by ID
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<?> getHospitalById(@PathVariable int id) {
        Hospital hospital = hospitalService.getHospitalById(id);
        if (hospital == null) {
            return new ResponseEntity<>("Hospital not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }

    // ✅ Update hospital by ID
    @PutMapping("/hospitals/{id}")
    public ResponseEntity<?> updateHospital(@PathVariable int id, @RequestBody Hospital hospital) {
        Hospital updatedHospital = hospitalService.updateHospital(id, hospital);
        if (updatedHospital == null) {
            return new ResponseEntity<>("Hospital not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedHospital, HttpStatus.OK);
    }

    // ✅ Duplicate field validation endpoints
    @GetMapping("/findusername/{username}")
    public boolean checkUsername(@PathVariable String username) {
        return hospitalService.getHospitalByUsername(username) != null;
    }

    @GetMapping("/findemail/{email}")
    public boolean checkEmail(@PathVariable String email) {
        return hospitalService.getHospitalByEmail(email) != null;
    }

    @GetMapping("/findcontact/{contact}")
    public boolean checkContact(@PathVariable String contact) {
        return hospitalService.getHospitalByContact(contact) != null;
    }

    @GetMapping("/findname/{name}")
    public boolean checkName(@PathVariable String name) {
        return hospitalService.getHospitalByName(name) != null;
    }

    // ✅ Blood Request Management APIs
    @PostMapping("/blood-requests")
    public ResponseEntity<RequestBlood> createRequest(@RequestBody RequestBlood requestBlood) {
    	
        RequestBlood created = hospitalService.createRequest(requestBlood);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/blood-requests")
    public ResponseEntity<List<RequestBlood>> getAllRequests() {
        return ResponseEntity.ok(hospitalService.getAllRequests());
    }

    @GetMapping("/blood-requests/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        RequestBlood request = hospitalService.getRequestById(id);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found.");
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/blood-requests/hospital/{username}")
    public ResponseEntity<List<RequestBlood>> getRequestsByHospitalUsername(@PathVariable String username) {
        try {
            // Fetch all requests for that hospital
            List<RequestBlood> requests = hospitalService.getRequestsByHospitalUsername(username);

            for (RequestBlood request : requests) {
                // Fetch all contributions for this request
                List<ContributedBloodBank> bloodBanks = hospitalService.getContributionsByRequest(request);

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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/blood-requests/status/{status}")
    public ResponseEntity<List<RequestBlood>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(hospitalService.getRequestsByStatus(status));
    }

    @PutMapping("/blood-requests/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody RequestBlood requestBlood) {
        RequestBlood updated = hospitalService.updateRequest(id, requestBlood);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found.");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/blood-requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        hospitalService.deleteRequest(id);
        return ResponseEntity.ok("Request deleted successfully!");
    }

    // ✅ Blood availability check
    @GetMapping("/blood-availability/{bloodType}")
    public ResponseEntity<List<BloodData>> getAvailabilityByType(@PathVariable String bloodType) {
        try {
            List<BloodData> data = hospitalService.getAvailabilityByType(bloodType);
            if (data.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Accepted requests for a specific hospital
    @GetMapping("/blood-requests/hospital/{username}/accepted")
    public ResponseEntity<List<RequestBlood>> getAcceptedRequestsByHospital(@PathVariable String username) {
        List<RequestBlood> accepted = hospitalService.getAcceptedRequestsByHospital(username);
        if (accepted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accepted);
        }
        return ResponseEntity.ok(accepted);
    }
    
 // ✅ API to display states and cities coverage
    @GetMapping("/coverage")
    public ResponseEntity<Map<String, Object>> getStateCityCoverage() {
        Map<String, Object> coverage = hospitalService.getStateCityCoverage();
        if (coverage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(coverage);
    }
    
    @GetMapping("/findallfeedbacks")
    public List<Feedback> findAllFeedbacks() {
        return hospitalService.findAllFeedbacks();
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
            hospitalService.addResponse(u, u.getQuestion().getId());
        }

        return ResponseEntity.ok("Responses submitted successfully");
    }

}
