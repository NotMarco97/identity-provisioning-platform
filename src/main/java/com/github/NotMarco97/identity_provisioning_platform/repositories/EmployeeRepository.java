package com.github.NotMarco97.identity_provisioning_platform.repositories;

import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    boolean existsByEmail(String email);
}
