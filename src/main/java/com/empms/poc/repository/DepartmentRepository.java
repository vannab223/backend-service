package com.empms.poc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empms.poc.models.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
	//Optional<Department> findByName(String name);
	
	@Query("SELECT d FROM Department d WHERE d.name = :name")
	Optional<Department> findByName(@Param("name") String name);

	@Query("SELECT d FROM Department d")
	List<Department> findAllDepartments();

	@Query("SELECT d FROM Department d WHERE d.id = :id")
	Optional<Department> findDepartmentById(@Param("id") Long id);

	@Modifying
	@Query("DELETE FROM Department d WHERE d.id = :id")
	void deleteById(@Param("id") Long id);
}
