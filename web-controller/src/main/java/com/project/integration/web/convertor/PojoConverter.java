package com.project.integration.web.convertor;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.User;
import com.project.integration.web.formmodel.OrderForm;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.sql.rowset.serial.SerialBlob;

@UtilityClass
public class PojoConverter {
    @SneakyThrows
    public Order convertPojoToDto(OrderForm orderForm){
        return new Order(new User(
                orderForm.getFirstName(),
                orderForm.getLastName(),
                orderForm.getPhone(),
                orderForm.getEmail()), new SerialBlob(orderForm.getDocument().getBytes()));
    }
}
