package com.project.integration.dao.repos;

import com.project.integration.dao.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  List<Employee> findByUserStatusNot(String status);

  @Query(nativeQuery = true, value = "select * from employees where employees.user_id = ?1")
  Optional<Employee> findByUserId(Integer userId);
}
