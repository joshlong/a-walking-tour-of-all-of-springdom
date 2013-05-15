package com.joshlong.spring.walkingtour.mobileweb;

import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Controller
@RequestMapping
public class RestCustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.POST)
    @ResponseBody
    public Customer updateCustomer(@RequestBody Customer customer) {
        return this.customerService.updateCustomer(customer.getId(), customer.getFirstName(), customer.getLastName());
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public Customer loadCustomerById(@PathVariable("customerId") long customerId) {
        return this.customerService.getCustomerById(BigInteger.valueOf(customerId));
    }

    @RequestMapping(value = "/customers", method = RequestMethod.PUT)
    @ResponseBody
    public Customer addCustomer(@RequestBody Customer customer) {
        return this.customerService.createCustomer(customer.getFirstName(), customer.getLastName());
    }



}
