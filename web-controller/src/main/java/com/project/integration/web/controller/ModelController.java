package com.project.integration.web.controller;

import com.project.integration.serv.services.OrderService;
import com.project.integration.serv.services.RoleService;
import com.project.integration.web.convertor.PojoConverter;
import com.project.integration.web.formmodel.OrderForm;
import com.project.integration.web.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class ModelController {
    private final RoleService roleService;
    private final OrderService orderService;
    @Autowired
    public ModelController(RoleService roleService, OrderService orderService) {
        this.roleService = roleService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String landingHome(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "landing_home";
    }

    @GetMapping("/check")
    @ResponseBody
    public boolean check() {
        return roleService.findByName("q");
    }

    @RequestMapping(value = "/create_request", method = RequestMethod.POST)
    public String createOrder(@ModelAttribute("orderForm") @Valid OrderForm orderForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
        } else {
            orderService.create(PojoConverter.convertPojoToDto(orderForm));
        }
        return "landing_home";
    }
}
