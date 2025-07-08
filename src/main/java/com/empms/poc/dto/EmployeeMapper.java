package com.empms.poc.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.empms.poc.models.Employee;

public class EmployeeMapper {

	public static EmployeeResponseDTO toDTO(Employee emp) {
		if (emp == null)
			return null;

		AddressDTO addressDTO = null;
		if (emp.getAddress() != null) {
			addressDTO = new AddressDTO(emp.getAddress().getStreet(), emp.getAddress().getCity(),
					emp.getAddress().getState(), emp.getAddress().getCountry(), emp.getAddress().getZipCode());
		}

		DepartmentDTO departmentDTO = null;
		if (emp.getDepartment() != null) {
			departmentDTO = new DepartmentDTO(emp.getDepartment().getId(), emp.getDepartment().getName(),
					emp.getDepartment().getDescription());
		}

		Set<String> roleNames = emp.getRoles().stream().map(role -> role.getName().name()) // assuming role.getName()
																							// returns ERole enum
				.collect(Collectors.toSet());
		return new EmployeeResponseDTO(emp.getId(), emp.getUsername(), emp.getEmail(), emp.getYearsOfExperience(),
				emp.getSalary(), emp.isEnabled(), addressDTO, departmentDTO, roleNames);
	}

}
