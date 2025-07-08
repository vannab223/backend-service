package com.empms.poc.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

	private String username;

	private String email;

	private Integer yearsOfExperience;
	
	private Double salary;

	private AddressDTO address;

	private Long departmentId;

	private Set<String> roles;

}
