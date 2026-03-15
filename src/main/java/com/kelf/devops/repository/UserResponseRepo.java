package com.kelf.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelf.devops.model.UserResponse;

@Repository
public interface UserResponseRepo extends JpaRepository<UserResponse, Integer> {

}
