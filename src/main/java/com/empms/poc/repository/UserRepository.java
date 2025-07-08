package com.empms.poc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empms.poc.models.Employee;

@Repository
public interface UserRepository extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	
	@Query("SELECT e FROM Employee e")
	Page<Employee> findAllEmployees(Pageable pageable);
	
	@Query("SELECT e FROM Employee e WHERE e.id = :id")
	Optional<Employee> findEmployeeById(@Param("id") Long id);
	
	@Modifying
	@Query("DELETE FROM Employee e WHERE e.id = :id")
	void deleteByEmployeeId(@Param("id") Long id);


	@Query("SELECT e FROM Employee e WHERE e.department.id = :deptId")
	List<Employee> findByDepartmentId(@Param("deptId") Long deptId);


	@Query("SELECT DISTINCT e FROM Employee e JOIN e.roles r WHERE r.name IN :roleNames")
	List<Employee> findByRoleNames(@Param("roleNames") List<String> roleNames);


	@Query("SELECT e FROM Employee e WHERE e.yearsOfExperience BETWEEN :min AND :max")
	List<Employee> findByExperienceRange(@Param("min") int min, @Param("max") int max);

	@Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
	List<Employee> findBySalaryRange(@Param("min") double min, @Param("max") double max);

}
