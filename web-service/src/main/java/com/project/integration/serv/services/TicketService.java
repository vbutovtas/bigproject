package com.project.integration.serv.services;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.dao.repos.TicketRepository;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
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
  private final ModelMapper modelMapper;

  @Autowired
  public TicketService(TicketRepository ticketRepository, ModelMapper modelMapper) {
    this.ticketRepository = ticketRepository;
    this.modelMapper = modelMapper;
    Converter<Integer, TicketSeverity> toUppercase =
        ctx -> TicketSeverity.getEnumByValue(ctx.getSource());
    TypeMap<Ticket, TicketDto> propertyMapper =
        this.modelMapper.createTypeMap(Ticket.class, TicketDto.class);
    // add deep mapping to flatten source's Player object into a single field in destination
    propertyMapper.addMappings(
        mapper -> mapper.using(toUppercase).map(Ticket::getSeverity, TicketDto::setSeverity));
  }

  public <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
    return list.stream().map(converter).collect(Collectors.toList());
  }

  public List<TicketDto> convertToDto(List<Ticket> ticketList) {
    return convertList(ticketList, this::convertToDto);
  }

  public TicketDto convertToDto(Ticket ticket) {
    return modelMapper.map(ticket, TicketDto.class);
  }

  public List<TicketDto> getProjectTickets(Integer projectId) {
    Optional<Ticket> project = ticketRepository.findById(projectId);
    if (project.isPresent()) {
      List<Ticket> tickets = ticketRepository.findByTicketOrderByOrder(project.get());
      // TODO convert to dtos
      return convertToDto(tickets);
    } else throw new RuntimeException("project does not exist"); // TODO
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
    } else throw new RuntimeException("ticket does not exist"); // TODO
  }
}
