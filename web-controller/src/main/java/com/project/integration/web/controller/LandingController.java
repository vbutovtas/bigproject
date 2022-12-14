package com.project.integration.web.controller;

import com.project.integration.serv.services.OrderService;
import com.project.integration.web.consts.ControllerConstants;
import com.project.integration.web.convertor.PojoConverter;
import com.project.integration.web.formmodel.OrderForm;
import com.project.integration.web.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class LandingController {
    private final OrderService orderService;

    @Autowired
    public LandingController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String landingHome(Model model) {
        model.addAttribute(ControllerConstants.ModelAttributes.ORDER_FORM, new OrderForm());
        return "landing_home";
    }

    @RequestMapping(value = "/create_request", method = RequestMethod.POST)
    public String createOrder(@ModelAttribute(ControllerConstants.ModelAttributes.ORDER_FORM) @Valid OrderForm orderForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute(ControllerConstants.ModelAttributes.IS_SHOWED, true);
            model.addAttribute(ControllerConstants.ModelAttributes.FORM_CORRECTOR, 580 + errorMap.size() * 14);
        } else {
            model.addAttribute(ControllerConstants.ModelAttributes.IS_SUCCESS, true);
            orderService.create(PojoConverter.convertOrderPojoToDto(orderForm));
        }
        return "landing_home";
    }
}
