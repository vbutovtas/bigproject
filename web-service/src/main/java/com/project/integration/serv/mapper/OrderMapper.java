package com.project.integration.serv.mapper;

import static com.project.integration.serv.mapper.Utils.convertList;

import com.project.integration.dao.entity.Order;
import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.exception.ServiceException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.project.integration.serv")
public class OrderMapper {
  private final ModelMapper modelMapper;

  @Autowired
  public OrderMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;

    TypeMap<Order, OrderDto> orderMapperToDto =
        this.modelMapper.createTypeMap(Order.class, OrderDto.class);

    TypeMap<OrderDto, Order> orderMapperToEntity =
        this.modelMapper.createTypeMap(OrderDto.class, Order.class);

    Converter<Blob, Byte[]> descriptionBlobToBytes =
        ctx -> {
          Byte[] descriptionAsBytes = null;
          if (Objects.nonNull(ctx.getSource())) {
            try {
              byte[] description = ctx.getSource().getBytes(1, (int) ctx.getSource().length());
              descriptionAsBytes = ArrayUtils.toObject(description);
            } catch (SQLException e) {
              throw new ServiceException(e.getMessage());
            }
          }
          return descriptionAsBytes;
        };

    Converter<Byte[], Blob> descriptionBytesToBlob =
        ctx -> {
          Blob descriptionBlob = null;
          if (Objects.nonNull(ctx.getSource())) {
            try {
              Byte[] descriptionAsBytes = ctx.getSource();
              descriptionBlob = new SerialBlob(ArrayUtils.toPrimitive(descriptionAsBytes));
            } catch (SQLException e) {
              throw new ServiceException(e.getMessage());
            }
          }
          return descriptionBlob;
        };

    orderMapperToDto.addMappings(
        mapper ->
            mapper
                .using(descriptionBlobToBytes)
                .map(Order::getDescription, OrderDto::setDescription));

    orderMapperToEntity.addMappings(
        mapper ->
            mapper
                .using(descriptionBytesToBlob)
                .map(OrderDto::getDescription, Order::setDescription));
  }

  public Order convertToEntity(OrderDto orderDto) {
    return modelMapper.map(orderDto, Order.class);
  }

  public OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }

  public List<OrderDto> convertToDto(List<Order> orderList) {
    return convertList(orderList, this::convertToDto);
  }
}
