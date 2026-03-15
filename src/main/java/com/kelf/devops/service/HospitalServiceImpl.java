package com.kelf.devops.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelf.devops.model.BloodData;
import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.Feedback;
import com.kelf.devops.model.FeedbackQuestion;
import com.kelf.devops.model.Hospital;
import com.kelf.devops.model.RequestBlood;
import com.kelf.devops.model.UserResponse;
import com.kelf.devops.repository.BloodDataRepository;
import com.kelf.devops.repository.ContributedBBREPO;
import com.kelf.devops.repository.FeedbackQuestionRepo;
import com.kelf.devops.repository.FeedbackRepo;
import com.kelf.devops.repository.HospitalRepository;
import com.kelf.devops.repository.RequestBlooodRepisotory;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private ContributedBBREPO contributedRepo;
    
    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired
    private FeedbackQuestionRepo feedbackQuestionRepo;


    @Override
    public List<ContributedBloodBank> getContributionsByRequest(RequestBlood request) {
        return contributedRepo.findByRequest(request);
    }


    @Override
    public Hospital registerHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getHospitalByUsername(String username) {
        return hospitalRepository.findByUsername(username);
    }

    @Override
    public Hospital getHospitalByEmail(String email) {
        return hospitalRepository.findByEmail(email);
    }

    @Override
    public Hospital getHospitalByContact(String contact) {
        return hospitalRepository.findByContact(contact);
    }

    @Override
    public Hospital getHospitalByName(String name) {
        return hospitalRepository.findByName(name);
    }

    @Override
    public Hospital loginHospital(String username, String password) {
        return hospitalRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Hospital getHospitalById(int id) {
        Optional<Hospital> hospitalOpt = hospitalRepository.findById(id);
        return hospitalOpt.orElse(null);
    }

    @Override
    public Hospital updateHospital(int id, Hospital hospital) {
        Optional<Hospital> existingHospitalOpt = hospitalRepository.findById(id);
        if (!existingHospitalOpt.isPresent()) {
            return null;
        }

        Hospital existingHospital = existingHospitalOpt.get();
        existingHospital.setUsername(hospital.getUsername());
        existingHospital.setName(hospital.getName());
        existingHospital.setOwnerName(hospital.getOwnerName());
        existingHospital.setType(hospital.getType());
        existingHospital.setAddress(hospital.getAddress());
        existingHospital.setState(hospital.getState());   // ✅ Added
        existingHospital.setCity(hospital.getCity());     // ✅ Added
        existingHospital.setContact(hospital.getContact());
        existingHospital.setEmail(hospital.getEmail());
        existingHospital.setLicenseNo(hospital.getLicenseNo());

        if (hospital.getPassword() != null && !hospital.getPassword().isEmpty()) {
            existingHospital.setPassword(hospital.getPassword());
        }

        return hospitalRepository.save(existingHospital);
    }

    @Autowired
    private RequestBlooodRepisotory requestBloodRepository;

    @Override
    public RequestBlood createRequest(RequestBlood requestBlood) {
        return requestBloodRepository.save(requestBlood);
    }

    @Override
    public List<RequestBlood> getAllRequests() {
        return requestBloodRepository.findAll();
    }

    @Override
    public RequestBlood getRequestById(Long id) {
        return requestBloodRepository.findById(id).orElse(null);
    }

    @Override
    public List<RequestBlood> getRequestsByHospitalUsername(String hospitalUsername) {
        return requestBloodRepository.findByHospitalUsername(hospitalUsername);
    }

    @Override
    public List<RequestBlood> getRequestsByStatus(String status) {
        return requestBloodRepository.findByStatusIgnoreCase(status);
    }

    @Override
    public RequestBlood updateRequest(Long id, RequestBlood requestBlood) {
        Optional<RequestBlood> optional = requestBloodRepository.findById(id);
        if (optional.isPresent()) {
            RequestBlood existing = optional.get();
            existing.setBloodGroup(requestBlood.getBloodGroup());
            existing.setUrgency(requestBlood.getUrgency());
            existing.setStatus(requestBlood.getStatus());
            existing.setDate(requestBlood.getDate());
            existing.setAcceptedOrg(requestBlood.getAcceptedOrg());
            return requestBloodRepository.save(existing);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRequest(Long id) {
        requestBloodRepository.deleteById(id);
    }

    @Autowired
    private BloodDataRepository bloodDataRepository;

    @Override
    public List<BloodData> getAvailabilityByType(String bloodType) {
        return bloodDataRepository.findByType1(bloodType);
    }

    @Override
    public List<RequestBlood> getAcceptedRequestsByHospital(String hospitalUsername) {
        return requestBloodRepository.findByHospitalUsernameAndStatusIgnoreCase(hospitalUsername, "ACCEPTED");
    }

    @Override
    public boolean blockHospital(int id) {
        Hospital hospital = hospitalRepository.findById(id).orElse(null);
        if (hospital != null) {
            hospital.setBlocked(!hospital.isBlocked());
            hospitalRepository.save(hospital);
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getStateCityCoverage() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Full state-city data
        Map<String, List<String>> stateCityData = new HashMap<>();
        stateCityData.put("Andhra Pradesh", Arrays.asList("Visakhapatnam", "Vijayawada", "Guntur", "Nellore", "Tirupati", "Kurnool", "Rajahmundry"));
        stateCityData.put("Telangana", Arrays.asList("Hyderabad", "Warangal", "Nizamabad", "Khammam", "Karimnagar", "Mahbubnagar"));
        stateCityData.put("Tamil Nadu", Arrays.asList("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Vellore"));
        stateCityData.put("Karnataka", Arrays.asList("Bengaluru", "Mysuru", "Mangalore", "Hubballi", "Belagavi"));
        stateCityData.put("Kerala", Arrays.asList("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kollam"));
        stateCityData.put("Maharashtra", Arrays.asList("Mumbai", "Pune", "Nagpur", "Nashik", "Thane"));
        stateCityData.put("Gujarat", Arrays.asList("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Gandhinagar"));
        stateCityData.put("Delhi", Arrays.asList("New Delhi", "Dwarka", "Rohini", "Saket", "Karol Bagh"));
        stateCityData.put("Uttar Pradesh", Arrays.asList("Lucknow", "Kanpur", "Varanasi", "Agra", "Prayagraj"));
        stateCityData.put("Madhya Pradesh", Arrays.asList("Bhopal", "Indore", "Jabalpur", "Gwalior"));
        stateCityData.put("Rajasthan", Arrays.asList("Jaipur", "Udaipur", "Jodhpur", "Kota"));
        stateCityData.put("Punjab", Arrays.asList("Amritsar", "Ludhiana", "Jalandhar", "Patiala"));
        stateCityData.put("Haryana", Arrays.asList("Gurugram", "Faridabad", "Panipat", "Karnal"));
        stateCityData.put("West Bengal", Arrays.asList("Kolkata", "Howrah", "Durgapur", "Siliguri"));
        stateCityData.put("Bihar", Arrays.asList("Patna", "Gaya", "Muzaffarpur", "Bhagalpur"));
        stateCityData.put("Odisha", Arrays.asList("Bhubaneswar", "Cuttack", "Rourkela", "Sambalpur"));
        stateCityData.put("Jharkhand", Arrays.asList("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro"));
        stateCityData.put("Assam", Arrays.asList("Guwahati", "Silchar", "Dibrugarh", "Jorhat"));
        stateCityData.put("Chhattisgarh", Arrays.asList("Raipur", "Bilaspur", "Durg", "Korba"));
        stateCityData.put("Goa", Arrays.asList("Panaji", "Margao", "Vasco da Gama"));
        stateCityData.put("Uttarakhand", Arrays.asList("Dehradun", "Haridwar", "Rishikesh", "Haldwani"));
        stateCityData.put("Himachal Pradesh", Arrays.asList("Shimla", "Mandi", "Dharamshala", "Solan"));
        stateCityData.put("Tripura", Arrays.asList("Agartala"));
        stateCityData.put("Meghalaya", Arrays.asList("Shillong"));
        stateCityData.put("Nagaland", Arrays.asList("Kohima", "Dimapur"));
        stateCityData.put("Manipur", Arrays.asList("Imphal"));
        stateCityData.put("Sikkim", Arrays.asList("Gangtok"));

        // Get all states used in hospitals
        List<String> usedStates = hospitalRepository.findDistinctStates();

        for (String state : usedStates) {
            List<String> usedCities = hospitalRepository.findCitiesByState(state);
            List<String> allCities = stateCityData.getOrDefault(state, Collections.emptyList());

            double coverage = allCities.isEmpty() ? 0 : (double) usedCities.size() / (double) allCities.size();

            if (coverage >= 0.5) {
                result.put(state, "✅ Active (50%+ cities)");
            } else {
                result.put(state, usedCities);
            }
        }

        return result;
    }
    
    @Override
    public List<Feedback> findAllFeedbacks() {
        // Fetch all feedbacks where org type is "hospital"
        return feedbackRepo.findByOrg("hospital");
    }

    @Override
    public void addResponse(UserResponse response, int questionId) {
        FeedbackQuestion question = feedbackQuestionRepo.findById(questionId).orElse(null);
        if (question != null) {
            response.setQuestion(question);
            if (question.getUsers() == null) {
                question.setUsers(new ArrayList<>());
            }
            question.getUsers().add(response);
            feedbackQuestionRepo.save(question);
        } else {
            throw new RuntimeException("Question not found with id: " + questionId);
        }
    }
}
