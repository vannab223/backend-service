package com.empms.poc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empms.poc.models.ERole;
import com.empms.poc.models.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	Optional<UserRole> findByName(ERole name);
	UserRole save(UserRole role);
}
