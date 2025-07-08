package com.empms.poc.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

	private Long id;
	private String username;
	private String email;
	private Integer yearsOfExperience;
	private Double salary;
	private boolean enabled;

	private AddressDTO address;
	private DepartmentDTO department;
	private Set<String> roles;

}
