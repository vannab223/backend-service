package com.empms.poc.security.serviceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empms.poc.dto.AddressDTO;
import com.empms.poc.dto.DepartmentDTO;
import com.empms.poc.dto.EmployeeRequestDTO;
import com.empms.poc.dto.EmployeeResponseDTO;
import com.empms.poc.exception.ResourceNotFoundException;
import com.empms.poc.models.Address;
import com.empms.poc.models.Department;
import com.empms.poc.models.Employee;
import com.empms.poc.repository.DepartmentRepository;
import com.empms.poc.repository.UserRepository;
import com.empms.poc.security.service.EmployeeService;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private UserRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Cacheable(value = "employees")
	public Page<Employee> getAllEmployees(Pageable pageable) {
		Page<Employee> page = employeeRepository.findAllEmployees(pageable);

		if (page.isEmpty()) {
			throw new ResourceNotFoundException("No employees found");
		}

		return page;
	}

	@Cacheable(value = "employee", key = "#id")
	public EmployeeResponseDTO getEmployeeById(Long id) {
		Employee emp = employeeRepository.findEmployeeById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + id + " not found."));
		return toEmployeeResponseDTO(emp);
	}

	@CacheEvict(value = { "employees", "employee" }, allEntries = true)
	public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeDetails) {
		Employee employee = employeeRepository.findEmployeeById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + id + " not found"));

		employee.setEmail(employeeDetails.getEmail());
		employee.setYearsOfExperience(employeeDetails.getYearsOfExperience());
		employee.setSalary(employeeDetails.getSalary());

		if (employeeDetails.getAddress() != null) {
			Address address = new Address();
			address.setStreet(employeeDetails.getAddress().getStreet());
			address.setCity(employeeDetails.getAddress().getCity());
			address.setState(employeeDetails.getAddress().getState());
			address.setZipCode(employeeDetails.getAddress().getZipCode());
			employee.setAddress(address);
		}

		if (employeeDetails.getDepartmentId() != null) {
			Department department = departmentRepository.findById(employeeDetails.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Department with ID " + employeeDetails.getDepartmentId() + " not found"));
			employee.setDepartment(department);
		} else {
			employee.setDepartment(null);
		}

		employee.setRoles(employee.getRoles());

		Employee saved = employeeRepository.save(employee);

		return toEmployeeResponseDTO(saved);
	}

	@CacheEvict(value = { "employees", "employee" }, allEntries = true)
	public boolean deleteEmployee(Long id) {
		Employee employee = employeeRepository.findEmployeeById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + id + " not found"));

		employeeRepository.deleteByEmployeeId(employee.getId());
		return true;
	}

	public Employee assignDepartmentToEmployee(Long empId, Long deptId) {
		Employee employee = employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + empId + " not found"));
		Department department = departmentRepository.findById(deptId)
				.orElseThrow(() -> new ResourceNotFoundException("Department with ID " + deptId + " not found"));
		employee.setDepartment(department);
		return employeeRepository.save(employee);
	}

	public List<Employee> getEmployeesByDepartment(Long deptId) {
		return employeeRepository.findByDepartmentId(deptId);
	}

	public List<Employee> getEmployeesByRoles(List<String> roleNames) {
		return employeeRepository.findByRoleNames(roleNames);
	}

	public List<Employee> getEmployeesByYearsOfExperience(int min, int max) {
		return employeeRepository.findByExperienceRange(min, max);
	}

	public List<Employee> getEmployeesBySalary(double min, double max) {
		return employeeRepository.findBySalaryRange(min, max);
	}

	private EmployeeResponseDTO toEmployeeResponseDTO(Employee emp) {
		EmployeeResponseDTO dto = new EmployeeResponseDTO();
		dto.setId(emp.getId());
		dto.setUsername(emp.getUsername());
		dto.setEmail(emp.getEmail());
		dto.setYearsOfExperience(emp.getYearsOfExperience());
		dto.setSalary(emp.getSalary());
		dto.setEnabled(emp.isEnabled());

		if (emp.getAddress() != null) {
			AddressDTO addressDTO = new AddressDTO();
			addressDTO.setStreet(emp.getAddress().getStreet());
			addressDTO.setCity(emp.getAddress().getCity());
			addressDTO.setState(emp.getAddress().getState());
			addressDTO.setCountry(emp.getAddress().getCountry());
			addressDTO.setZipCode(emp.getAddress().getZipCode());
			dto.setAddress(addressDTO);
		}

		if (emp.getDepartment() != null) {
			DepartmentDTO departmentDTO = new DepartmentDTO();
			departmentDTO.setId(emp.getDepartment().getId());
			departmentDTO.setName(emp.getDepartment().getName());
			departmentDTO.setDescription(emp.getDepartment().getDescription());
			dto.setDepartment(departmentDTO);
		}

		if (emp.getRoles() != null) {
			Set<String> roleNames = emp.getRoles().stream().map(role -> role.getName().name())
					.collect(Collectors.toSet());
			dto.setRoles(roleNames);
		}

		return dto;
	}

}
