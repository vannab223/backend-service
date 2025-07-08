package com.empms.poc.security.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.empms.poc.dto.DepartmentDTO;
import com.empms.poc.dto.DepartmentResponseDTO;
import com.empms.poc.models.Department;
import com.empms.poc.repository.DepartmentRepository;
import com.empms.poc.repository.UserRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private UserRepository employeeRepository;

	public Department createDepartment(DepartmentDTO departmentDTO) {
		Department department = convertToEntity(departmentDTO);
		return departmentRepository.save(department);
	}

	@Cacheable("departments")
	public List<Department> getAllDepartments() {
		return departmentRepository.findAllDepartments();
	}

	@Cacheable(value = "department", key = "#id")
	public Optional<Department> getDepartmentById(Long id) {
		return departmentRepository.findDepartmentById(id);
	}

	@CacheEvict(value = { "departments", "department" }, allEntries = true)
	public Department updateDepartment(Long id, DepartmentDTO departmentDTO) {
		Department updated = convertToEntity(departmentDTO);
		updated.setId(id);
		return departmentRepository.save(updated);
	}

	@CacheEvict(value = { "departments", "department" }, allEntries = true)
	public void deleteDepartment(Long id) {
		departmentRepository.findDepartmentById(id).ifPresent(department -> {
			departmentRepository.deleteById(id);
		});
	}

	public Department convertToEntity(DepartmentDTO dto) {
		Department dept = new Department();
		dept.setId(dto.getId());
		dept.setName(dto.getName());
		dept.setDescription(dto.getDescription());
		return dept;
	}

	public DepartmentResponseDTO convertToResponseDTO(Department department) {
		return new DepartmentResponseDTO(department.getName(), department.getDescription());
	}

}
