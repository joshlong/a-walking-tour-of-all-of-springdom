package com.joshlong.spring.walkingtour.mobileweb;

import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@Controller
public class CustomerController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private CustomerService customerService;

    // so that Spring MVC has something to bind form values to
    @ModelAttribute
    public Customer customer() {
        return new Customer();
    }



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String customer(@RequestParam(value = "id", required = false) Long id, Device device, Model model) {
        if (null != id) {
            model.addAttribute("customer", this.customerService.getCustomerById(BigInteger.valueOf(id)));
            model.addAttribute("customerId", id);
        }

        String typeOfDevice = device.isNormal() ? "normal" : (device.isTablet() ? "tablet" : "phone");
        model.addAttribute("typeOfDevice", typeOfDevice);

        return "customers/display";

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void setupAddition() {
        log.info("About to show the add page, which will be 'add.jsp'");
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public String processAddition(@ModelAttribute Customer c, Model model) {
        Customer customer = this.customerService.createCustomer(c.getFirstName(), c.getLastName());
        model.addAttribute("id", customer.getId());
        return "redirect:/display";
    }



}


