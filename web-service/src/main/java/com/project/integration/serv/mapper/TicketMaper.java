package com.project.integration.serv.mapper;

import static com.project.integration.serv.mapper.Utils.convertList;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.entity.Ticket;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketSeverity;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.project.integration.serv")
public class TicketMaper {
  private final ModelMapper modelMapper;

  @Autowired
  public TicketMaper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;

    TypeMap<Ticket, TicketDto> ticketMapper =
        this.modelMapper.createTypeMap(Ticket.class, TicketDto.class);
    Converter<Integer, TicketSeverity> severityConverter =
        ctx -> TicketSeverity.getEnumByValue(ctx.getSource());
    ticketMapper.addMappings(
        mapper -> mapper.using(severityConverter).map(Ticket::getSeverity, TicketDto::setSeverity));

//    Converter<Blob, String> photoConverter = ctx -> {
//      try {
//        if(Objects.nonNull(ctx.getSource())){
//          byte[] photoAsBytes = ctx.getSource().getBytes(1, (int) ctx.getSource().length());
//          return Base64.getEncoder().encodeToString(photoAsBytes);
//        } else return "";
//      } catch (Exception e) {
//        throw new RuntimeException(e); // TODO
//      }
//    };
//
//    ticketMapper.addMappings(
//        mapper -> mapper.using(photoConverter).map(src -> src.getAssignee().getPhoto(), TicketDto::getAssignee));

//    modelMapper.addMappings(new PropertyMap<Ticket, TicketDto>() {
//      @Override
//      protected void configure() {
//        if(Objects.nonNull(source.getAssignee().getPhoto())){
//          try{
//            byte[] photoAsBytes = source.getAssignee().getPhoto().getBytes(1, (int) source.getAssignee().getPhoto().length());
//            String base64String = Base64.getEncoder().encodeToString(photoAsBytes);
//            map().getAssignee().setPhoto(base64String);
//          } catch (SQLException e){
//            throw new RuntimeException(e); //TODO
//          }
//        }
//      }
//    });

  }

  public List<TicketDto> convertToDto(List<Ticket> ticketList) {
    return convertList(ticketList, this::convertToDto);
  }

  public TicketDto convertToDto(Ticket ticket) {
    String base64String = "";
    if(Objects.nonNull(ticket.getAssignee().getPhoto())){
      try{
        byte[] photoAsBytes = ticket.getAssignee().getPhoto().getBytes(1, (int) ticket.getAssignee().getPhoto().length());
        base64String = Base64.getEncoder().encodeToString(photoAsBytes);
      } catch (SQLException e){
        throw new RuntimeException(e); //TODO
      }
    }
    TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
    ticketDto.getAssignee().setPhoto(base64String);
    return ticketDto;
  }
}