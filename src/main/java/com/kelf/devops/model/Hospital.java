package com.kelf.devops.model;

import java.util.concurrent.ThreadLocalRandom;
import jakarta.persistence.*;

@Entity
@Table(name = "hospital_table")
public class Hospital {

    @Id
    @Column(name = "hospital_id")
    private int id;

    @Column(name = "hospital_username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "hospital_name", nullable = false, length = 100)
    private String name;

    @Column(name = "hospital_owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(name = "hospital_type", nullable = false, length = 50)
    private String type;

    @Column(name = "hospital_address", nullable = false, length = 200)
    private String address;

    @Column(name = "hospital_contact", nullable = false, length = 20)
    private String contact;

    @Column(name = "hospital_email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "hospital_license_no", nullable = false, length = 30)
    private String licenseNo;

    @Column(name = "hospital_password", nullable = false, length = 100)
    private String password;

    @Column(name = "hospital_blocked", nullable = false)
    private boolean blocked = false;   // ✅ new field for account blocking
    
    @Column(name = "hospital_state", length = 50)
    private String state;

    @Column(name = "hospital_city", length = 50)
    private String city;


    @PrePersist
    public void generateRandomId() {
        if (this.id == 0) {
            this.id = ThreadLocalRandom.current().nextInt(1000, 1000000);
        }
    }



	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getLicenseNo() {
		return licenseNo;
	}


	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
    public boolean isBlocked() { 
    	return blocked; 
    }
    public void setBlocked(boolean blocked) { 
    	this.blocked = blocked; 
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Hospital [id=" + id 
               + ", username=" + username 
               + ", name=" + name 
               + ", ownerName=" + ownerName 
               + ", type=" + type 
               + ", address=" + address 
               + ", city=" + city
               + ", state=" + state
               + ", contact=" + contact 
               + ", email=" + email 
               + ", licenseNo=" + licenseNo 
               + ", password=" + password 
               + ", blocked=" + blocked + "]";
    }

}
