package com.empms.poc.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.empms.poc.dto.EmployeeRequestDTO;
import com.empms.poc.dto.EmployeeResponseDTO;
import com.empms.poc.models.Employee;

public interface EmployeeService {

	Page<Employee> getAllEmployees(Pageable pageable);

	EmployeeResponseDTO getEmployeeById(Long id);

	EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeDetails);

	boolean deleteEmployee(Long id);

}
