package com.project.integration.web.controller;

import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.services.TicketService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/project")
public class TicketController {
  private final TicketService ticketService;

  @Autowired
  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @GetMapping(value = "/{id}/tickets")
  public ResponseEntity<List<TicketDto>> getProjectTickets(@PathVariable("id") Integer id) {
    List<TicketDto> tickets = ticketService.getProjectTickets(id);
    return new ResponseEntity<>(tickets, HttpStatus.OK);
  }

  @PutMapping(value = "/reorder")
  public ResponseEntity<Void> reorderTickets(
      @RequestParam Integer id,
      @RequestParam Integer destination,
      @RequestParam String destinationColumn) {
    ticketService.reorder(id, destination, TicketStatus.getEnumByValue(destinationColumn));
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
