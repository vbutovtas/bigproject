package com.project.integration.web.controller;

import com.project.integration.serv.services.OrderService;
import com.project.integration.web.convertor.PojoConverter;
import com.project.integration.web.formmodel.OrderForm;
import com.project.integration.web.utils.ControllerUtils;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ModelController {
    private final OrderService orderService;

    @Autowired
    public ModelController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String landingHome(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "landing_home";
    }

    @RequestMapping(value = "/create_request", method = RequestMethod.POST)
    public String createOrder(@ModelAttribute("orderForm") @Valid OrderForm orderForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("isShowed", true);
            model.addAttribute("formCorrector", 560 + errorMap.size() * 14);
        } else {
            orderService.create(PojoConverter.convertOrderPojoToDto(orderForm));
        }
        return "landing_home";
    }
}
