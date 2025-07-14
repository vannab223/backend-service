package com.empms.poc.payload.request;

import java.util.Set;

import com.empms.poc.models.Address;
import com.empms.poc.models.Department;
import com.empms.poc.models.Employee;
import com.empms.poc.models.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Integer yearsOfExperience;
	private Double salary;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	private Address address;
	private Set<String> role;

	private Long departmentId;

	
}
