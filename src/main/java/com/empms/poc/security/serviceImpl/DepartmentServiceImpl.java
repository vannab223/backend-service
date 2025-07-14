package com.empms.poc.security.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.empms.poc.dto.DepartmentDTO;
import com.empms.poc.dto.DepartmentResponseDTO;
import com.empms.poc.exception.ResourceNotFoundException;
import com.empms.poc.models.Department;
import com.empms.poc.repository.DepartmentRepository;
import com.empms.poc.security.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department createDepartment(DepartmentDTO departmentDTO) {
		if (departmentDTO == null || departmentDTO.getName() == null) {
			throw new IllegalArgumentException("Department name must not be null.");
		}

		Department department = convertToEntity(departmentDTO);
		return departmentRepository.save(department);
	}

	@Cacheable("departments")
	public List<Department> getAllDepartments() {
		return departmentRepository.findAllDepartments();
	}

	@Cacheable(value = "department", key = "#id")
	public Optional<Department> getDepartmentById(Long id) {
		return departmentRepository.findDepartmentById(id).or(() -> {
			throw new ResourceNotFoundException("Department with ID " + id + " not found");
		});
	}

	@CacheEvict(value = { "departments", "department" }, allEntries = true)
	public Department updateDepartment(Long id, DepartmentDTO departmentDTO) {
		departmentRepository.findDepartmentById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot update. Department ID " + id + " not found."));

		Department updated = convertToEntity(departmentDTO);
		updated.setId(id);
		return departmentRepository.save(updated);
	}

	@CacheEvict(value = { "departments", "department" }, allEntries = true)
	public void deleteDepartment(Long id) {
		Department dept = departmentRepository.findDepartmentById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Department ID " + id + " not found."));

		departmentRepository.deleteById(id);
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
