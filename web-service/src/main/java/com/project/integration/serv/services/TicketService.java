package com.project.integration.serv.services;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.dao.repos.TicketRepository;
import com.project.integration.serv.consts.Consts;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import com.project.integration.serv.mapper.TicketMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class TicketService {
  private final TicketRepository ticketRepository;
  private final TicketMapper ticketMapper;

  private final OrderService orderService;

  @Autowired
  public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper, OrderService orderService) {
    this.ticketRepository = ticketRepository;
    this.ticketMapper = ticketMapper;
    this.orderService = orderService;
  }

  public List<TicketDto> getProjectTickets(Integer projectId) {
    Optional<Ticket> project = ticketRepository.findById(projectId);
    if (project.isPresent()) {
      List<Ticket> tickets = ticketRepository.findByTicketOrderByOrder(project.get());
      return ticketMapper.convertToDto(tickets);
    } else throw new RuntimeException("project does not exist"); // TODO
  }

  public List<TicketDto> getProjects() {
    return ticketMapper.convertToDto(ticketRepository.findByType(TicketType.PROJECT.getValue()));
  }

  public TicketDto getTicket(Integer id) {
    Optional<Ticket> ticket = ticketRepository.findById(id);
    if (ticket.isPresent()) return ticketMapper.convertToDto(ticket.get());
    else throw new RuntimeException("ticket does not exist"); // TODO
  }

  public void updateTicket(TicketDto ticketDto, Integer id) {
    ticketDto.setId(id);
    Ticket ticket = ticketMapper.convertToEntity(ticketDto);
    prepareTicketForUpdate(ticket);
    createOrUpdate(ticket);
  }

  private void prepareTicketForUpdate(Ticket ticket) {
    Optional<Ticket> initialTicket = ticketRepository.findById(ticket.getId());
    if (initialTicket.isEmpty()) throw new RuntimeException("Ticket not found"); // TODO
    if (Objects.isNull(ticket.getAssignee())) ticket.setAssignee(initialTicket.get().getAssignee());
    if (Objects.isNull(ticket.getReporter())) ticket.setReporter(initialTicket.get().getReporter());
    if (Objects.isNull(ticket.getTicket())) ticket.setTicket(initialTicket.get().getTicket());
    if (Objects.isNull(ticket.getAssignee())) ticket.setAssignee(initialTicket.get().getAssignee());
    if (Objects.isNull(ticket.getName())) ticket.setName(initialTicket.get().getName());
    if (Objects.isNull(ticket.getDescription()))
      ticket.setDescription(initialTicket.get().getDescription());
    if (Objects.isNull(ticket.getCreateDate()))
      ticket.setCreateDate(initialTicket.get().getCreateDate());
    if (Objects.isNull(ticket.getDueDate())) ticket.setDueDate(initialTicket.get().getDueDate());
    if (Objects.isNull(ticket.getEstimatedTime()))
      ticket.setEstimatedTime(initialTicket.get().getEstimatedTime());
    if (Objects.isNull(ticket.getLoggedTime()))
      ticket.setLoggedTime(initialTicket.get().getLoggedTime());
    if (Objects.isNull(ticket.getStatus())) ticket.setStatus(initialTicket.get().getStatus());
    if (Objects.isNull(ticket.getSeverity())) ticket.setSeverity(initialTicket.get().getSeverity());
    if (Objects.isNull(ticket.getType())) ticket.setType(initialTicket.get().getType());
    if (Objects.isNull(ticket.getGitRef())) ticket.setGitRef(initialTicket.get().getGitRef());
    if (Objects.isNull(ticket.getOrder())) ticket.setOrder(initialTicket.get().getOrder());
    if (Objects.isNull(ticket.getComments())) ticket.setComments(initialTicket.get().getComments());
    if (Objects.isNull(ticket.getEmployees()))
      ticket.setEmployees(initialTicket.get().getEmployees());
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

  public void createProject(TicketDto projectDto, Integer orderId) {
    projectDto.setCreateDate(LocalDateTime.now());
    projectDto.setStatus(TicketStatus.OPEN);
    projectDto.setType(TicketType.PROJECT);
    projectDto.setSeverity(TicketSeverity.NORMAL);
    projectDto.setOrder(Consts.PROJECT_ORDER);
    Ticket project = ticketMapper.convertToEntity(projectDto);
    Integer projectId = createOrUpdate(project);
    orderService.setProjectToOrder(projectId, orderId);
  }

  public void createTicket(TicketDto ticketDto, Integer projectId) {
    ticketDto.setCreateDate(LocalDateTime.now());
    long order =
        ticketRepository.countByStatusAndOrderGreaterThan(
            ticketDto.getStatus().getValue(), Consts.PROJECT_ORDER);
    ticketDto.setOrder((int) order + 1);
    Ticket ticket = ticketMapper.convertToEntity(ticketDto);
    Ticket projectTicket = new Ticket();
    projectTicket.setId(projectId);
    ticket.setTicket(projectTicket);
    Integer id = createOrUpdate(ticket);
    reorder(id, 0, ticketDto.getStatus());
  }

  private Integer createOrUpdate(Ticket ticket) {
    try {
      ticketRepository.save(ticket);
      return ticket.getId();
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException(e); // TODO
    }
  }
}
