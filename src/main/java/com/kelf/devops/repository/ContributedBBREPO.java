package com.kelf.devops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelf.devops.model.ContributedBloodBank;
import com.kelf.devops.model.RequestBlood;

@Repository
public interface ContributedBBREPO  extends JpaRepository<ContributedBloodBank, Integer>{
	List<ContributedBloodBank> findByRequest(RequestBlood request);

}
