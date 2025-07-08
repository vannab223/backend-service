
package com.empms.poc.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empms.poc.dto.EmployeeMapper;
import com.empms.poc.dto.EmployeeRequestDTO;
import com.empms.poc.dto.EmployeeResponseDTO;
import com.empms.poc.models.Employee;
import com.empms.poc.security.services.EmployeeService;

/**
 * 
 * 
 * A PERSON WHO IS HAVING THE ADMIN ROLE can manage the employees and associated
 * resources such as address & department.
 * 
 * SELF user/emp can manage their own profile, such as ADDRESS and DEPARTMENT
 * 
 */
@RestController

@RequestMapping("emp")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/{id}")

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
		return new ResponseEntity<EmployeeResponseDTO>(employeeService.getEmployeeById(id), HttpStatus.OK);
	}

	@GetMapping

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(

			@RequestParam(defaultValue = "0") int page,

			@RequestParam(defaultValue = "10") int size,

			@RequestParam(defaultValue = "id") String sortBy) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Employee> employeePage = employeeService.getAllEmployees(pageable);

		Page<EmployeeResponseDTO> dtoPage = employeePage.map(EmployeeMapper::toDTO);

		return ResponseEntity.ok(dtoPage);
	}

	@PutMapping("/{id}")

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id,

			@RequestBody EmployeeRequestDTO employeeDetails) {
		EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/{id}")

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		boolean deleted = employeeService.deleteEmployee(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{empId}/department/{deptId}")

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Employee assignDepartmentToEmployee(@PathVariable Long empId, @PathVariable Long deptId) {
		return employeeService.assignDepartmentToEmployee(empId, deptId);
	}

}
