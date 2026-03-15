package com.kelf.devops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelf.devops.model.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    Hospital findByUsername(String username);
    Hospital findByEmail(String email);
    Hospital findByContact(String contact);
    Hospital findByName(String name);
    
    Hospital findByUsernameAndPassword(String username, String password);
    
    @Query("SELECT DISTINCT h.state FROM Hospital h")
    List<String> findDistinctStates();

    @Query("SELECT DISTINCT h.city FROM Hospital h WHERE h.state = :state")
    List<String> findCitiesByState(String state);
}
