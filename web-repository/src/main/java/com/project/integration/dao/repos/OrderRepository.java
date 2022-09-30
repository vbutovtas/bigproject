package com.project.integration.dao.repos;

import com.project.integration.dao.entity.Order;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ComponentScan("com.project.integration")
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
