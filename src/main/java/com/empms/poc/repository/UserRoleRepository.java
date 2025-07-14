package com.empms.poc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empms.poc.models.ERole;
import com.empms.poc.models.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	
	@Query("SELECT r FROM UserRole r WHERE r.name = :name")
    Optional<UserRole> findByName(@Param("name") ERole name);

	UserRole save(UserRole role);
}
