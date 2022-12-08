package com.project.integration.dao.repos;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.User;
import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@ComponentScan("com.project.integration")
public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByClient(User client);

  @Transactional
  @Modifying
  @Query(value="update orders set orders.project_id=?1 where orders.id=?2", nativeQuery = true)
  void setProject(Integer projectId, Integer orderId);
}
