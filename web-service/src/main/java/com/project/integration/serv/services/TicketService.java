package com.project.integration.serv.services;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.dao.repos.TicketRepository;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.mapper.TicketMaper;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.project.integration.serv.enums.TicketType;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class TicketService {
  private final TicketRepository ticketRepository;
  private final TicketMaper ticketMaper;

  @Autowired
  public TicketService(TicketRepository ticketRepository, TicketMaper ticketMaper) {
    this.ticketRepository = ticketRepository;
    this.ticketMaper = ticketMaper;
  }

  public List<TicketDto> getProjectTickets(Integer projectId) {
    Optional<Ticket> project = ticketRepository.findById(projectId);
    if (project.isPresent()) {
      List<Ticket> tickets = ticketRepository.findByTicketOrderByOrder(project.get());
      return ticketMaper.convertToDto(tickets);
    } else throw new RuntimeException("project does not exist"); //TODO
  }

  public List<TicketDto> getProjects(){
      return convertToDto(ticketRepository.findByType(TicketType.PROJECT.getValue()));
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
