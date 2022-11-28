package com.project.integration.serv.mapper;

import static com.project.integration.serv.mapper.Utils.convertList;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketSeverity;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.project.integration.serv")
public class TicketMapper {
  private final ModelMapper modelMapper;

  @Autowired
  public TicketMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;

    TypeMap<Ticket, TicketDto> ticketToDtoMapper =
        this.modelMapper.createTypeMap(Ticket.class, TicketDto.class);
    Converter<Integer, TicketSeverity> severityToDtoConverter =
        ctx -> TicketSeverity.getEnumByValue(ctx.getSource());
    ticketToDtoMapper.addMappings(
        mapper -> mapper.using(severityToDtoConverter).map(Ticket::getSeverity, TicketDto::setSeverity));

    TypeMap<TicketDto, Ticket> ticketToEntityMapper =
        this.modelMapper.createTypeMap(TicketDto.class, Ticket.class);
    Converter<TicketSeverity, Integer> severityToEntityConverter =
        ctx -> {
          System.out.println("1: " + ctx.getSource());
          System.out.println("2: " + ctx.getSource().getValue());
          return ctx.getSource().getValue();
        };
    ticketToEntityMapper.addMappings(
        mapper -> mapper.using(severityToEntityConverter).map(TicketDto::getSeverity, Ticket::setSeverity));
  }

  public Ticket convertToEntity(TicketDto ticketDto){
    return modelMapper.map(ticketDto, Ticket.class);
  }

  public List<TicketDto> convertToDto(List<Ticket> ticketList) {
    return convertList(ticketList, this::convertToDto);
  }

  public TicketDto convertToDto(Ticket ticket) {
    String base64StringAssignee = convertPhotoToBase64(ticket.getAssignee().getPhoto()),
        base64StringReporter = convertPhotoToBase64(ticket.getReporter().getPhoto());
    TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
    ticketDto.getAssignee().setPhoto(base64StringAssignee);
    ticketDto.getReporter().setPhoto(base64StringReporter);
    return ticketDto;
  }

  private String convertPhotoToBase64(Blob photo) {
    String base64String = "";
    if (Objects.nonNull(photo)) {
      try {
        byte[] photoAsBytes = photo.getBytes(1, (int) photo.length());
        base64String = Base64.getEncoder().encodeToString(photoAsBytes);
      } catch (SQLException e) {
        throw new RuntimeException(e); // TODO
      }
    }
    return base64String;
  }
}
