package com.empms.poc.security.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.Workbook;


import com.empms.poc.models.Employee;

@Component
public class EmployeeExcelService {

	public ByteArrayInputStream employeesToExcel(List<Employee> employees) throws IOException {
		String[] columns = { "ID", "Username", "Email", "Years of Experience", "Salary", "Department", "Roles" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Employees");

			// Header row
			Row headerRow = sheet.createRow(0);
			for (int col = 0; col < columns.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(columns[col]);
			}

			// Data rows
			int rowIdx = 1;
			for (Employee emp : employees) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(emp.getId());
				row.createCell(1).setCellValue(emp.getUsername());
				row.createCell(2).setCellValue(emp.getEmail());
				row.createCell(3).setCellValue(emp.getYearsOfExperience());
				row.createCell(4).setCellValue(emp.getSalary());

				// Department name or empty if null
				String deptName = emp.getDepartment() != null ? emp.getDepartment().getName() : "";
				row.createCell(5).setCellValue(deptName);

				// Roles concatenated string
				String roles = emp.getRoles() == null ? ""
						: emp.getRoles().stream().map(role -> role.getName().name()) // or role.getName() if String
								.reduce((r1, r2) -> r1 + ", " + r2).orElse("");
				row.createCell(6).setCellValue(roles);
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

}
