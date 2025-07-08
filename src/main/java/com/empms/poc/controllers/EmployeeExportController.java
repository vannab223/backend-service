package com.empms.poc.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empms.poc.models.Employee;
import com.empms.poc.security.services.EmployeeExcelService;
import com.empms.poc.security.services.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/employees/export")
public class EmployeeExportController {
	
	 @Autowired
	    private EmployeeService employeeService;

	    @Autowired
	    private EmployeeExcelService excelService;

	    @Operation(summary = "Export all employees data to Excel")
	    @GetMapping("/department/{deptId}")
	    public ResponseEntity<Resource> exportByDepartment(@PathVariable Long deptId) throws IOException {
	        List<Employee> employees = employeeService.getEmployeesByDepartment(deptId);
	        return buildExcelResponse(employees, "employees_by_department.xlsx");
	    }

	    @Operation(summary = "Export all employees data to Excel")
	    @GetMapping("/roles")
	    public ResponseEntity<Resource> exportByRoles(@RequestParam List<String> roleNames) throws IOException {
	        List<Employee> employees = employeeService.getEmployeesByRoles(roleNames);
	        return buildExcelResponse(employees, "employees_by_roles.xlsx");
	    }

	    @Operation(summary = "Export all employees data to Excel")
	    @GetMapping("/experience")
	    public ResponseEntity<Resource> exportByExperience(@RequestParam int minYears, @RequestParam int maxYears) throws IOException {
	        List<Employee> employees = employeeService.getEmployeesByYearsOfExperience(minYears, maxYears);
	        return buildExcelResponse(employees, "employees_by_experience.xlsx");
	    }

	    @Operation(summary = "Export all employees data to Excel")
	    @GetMapping("/salary")
	    public ResponseEntity<Resource> exportBySalary(@RequestParam double minSalary, @RequestParam double maxSalary) throws IOException {
	        List<Employee> employees = employeeService.getEmployeesBySalary(minSalary, maxSalary);
	        return buildExcelResponse(employees, "employees_by_salary.xlsx");
	    }

	    private ResponseEntity<Resource> buildExcelResponse(List<Employee> employees, String filename) throws IOException {
	        ByteArrayInputStream stream = excelService.employeesToExcel(employees);
	        InputStreamResource resource = new InputStreamResource(stream);

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	                .body(resource);
	    }

}
