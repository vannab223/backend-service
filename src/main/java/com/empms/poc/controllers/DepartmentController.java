package com.empms.poc.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empms.poc.dto.DepartmentDTO;
import com.empms.poc.dto.DepartmentResponseDTO;
import com.empms.poc.models.Department;
import com.empms.poc.security.services.DepartmentService;

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
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentResponseDTO> getDepartment(@PathVariable Long id) {
		Optional<Department> dept = departmentService.getDepartmentById(id);
		return dept.map(d -> ResponseEntity.ok(departmentService.convertToResponseDTO(d)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
		List<DepartmentResponseDTO> departments = departmentService.getAllDepartments().stream()
				.map(departmentService::convertToResponseDTO).collect(Collectors.toList());

		return ResponseEntity.ok(departments);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public DepartmentResponseDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
		Department department = departmentService.createDepartment(departmentDTO);
		return departmentService.convertToResponseDTO(department);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public DepartmentResponseDTO updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
		Department updated = departmentService.updateDepartment(id, departmentDTO);
		return departmentService.convertToResponseDTO(updated);
	}

}
