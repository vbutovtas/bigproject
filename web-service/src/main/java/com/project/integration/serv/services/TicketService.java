package com.project.integration.serv.services;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.dao.repos.TicketRepository;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.mapper.TicketMapper;
import java.util.List;
import java.util.Optional;

import com.project.integration.serv.enums.TicketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class TicketService {
  private final TicketRepository ticketRepository;
  private final TicketMapper ticketMapper;

  @Autowired
  public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
    this.ticketRepository = ticketRepository;
    this.ticketMapper = ticketMapper;
  }

  public List<TicketDto> getProjectTickets(Integer projectId) {
    Optional<Ticket> project = ticketRepository.findById(projectId);
    if (project.isPresent()) {
      List<Ticket> tickets = ticketRepository.findByTicketOrderByOrder(project.get());
      return ticketMapper.convertToDto(tickets);
    } else throw new RuntimeException("project does not exist"); //TODO
  }

  public List<TicketDto> getProjects(){
      return ticketMapper.convertToDto(ticketRepository.findByType(TicketType.PROJECT.getValue()));
  }

  public TicketDto getTicket(Integer id){
    Optional<Ticket> ticket = ticketRepository.findById(id);
    if(ticket.isPresent())
      return ticketMapper.convertToDto(ticket.get());
    else throw new RuntimeException("ticket does not exist"); //TODO
  }

  public void reorder(Integer id, Integer destination, TicketStatus destinationColumn) {
    Optional<Ticket> sourceTicket = ticketRepository.findById(id);
    if (sourceTicket.isPresent()) {
      ticketRepository.reorderTickets(
          sourceTicket.get().getId(),
          sourceTicket.get().getOrder(),
          sourceTicket.get().getStatus(),
          destination,
          destinationColumn.getValue());
    } else throw new RuntimeException("ticket does not exist"); //TODO
  }
}
