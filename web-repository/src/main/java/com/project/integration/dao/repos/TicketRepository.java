package com.project.integration.dao.repos;

import com.project.integration.dao.entity.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
  List<Ticket> findByTicketOrderByOrder(Ticket project);

  List<Ticket> findByType(String type);

  Optional<Ticket> findByOrder(Integer order);

  @Procedure(procedureName = "reorderTickets")
  void reorderTickets(
      @Param("id") Integer id,
      @Param("startOrder") Integer startOrder,
      @Param("startColumn") String startColumn,
      @Param("finishOrder") Integer finishOrder,
      @Param("finishColumn") String finishColumn);

  long countByStatusAndOrderGreaterThan(String status, Integer order);
}
