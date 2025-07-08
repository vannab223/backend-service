package com.empms.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.empms.poc.models.ERole;
import com.empms.poc.models.UserRole;
import com.empms.poc.repository.UserRoleRepository;

@SpringBootApplication
public class EmployeeManagementServiceApplication implements CommandLineRunner {

	@Autowired
	private UserRoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		roleRepository.save(new UserRole(1L, ERole.ROLE_USER));
		roleRepository.save(new UserRole(2L, ERole.ROLE_ADMIN));
	}

}
