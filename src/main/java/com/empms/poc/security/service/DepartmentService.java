package com.empms.poc.security.service;

import java.util.List;
import java.util.Optional;

import com.empms.poc.dto.DepartmentDTO;
import com.empms.poc.models.Department;

public interface DepartmentService {

	Department createDepartment(DepartmentDTO departmentDTO);

	List<Department> getAllDepartments();

	Optional<Department> getDepartmentById(Long id);

	Department updateDepartment(Long id, DepartmentDTO departmentDTO);

	void deleteDepartment(Long id);

}
