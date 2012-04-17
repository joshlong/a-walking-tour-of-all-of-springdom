package org.springsource.examples.sawt.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

@Controller
@RequestMapping(headers = "Accept=application/json, application/xml")
public class RestCustomerController {

    @Autowired private CustomerService customerService;

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.POST)
    @ResponseBody
    public Customer updateCustomer(@RequestBody Customer c) {
        return this.customerService.updateCustomer(c.getId(), c.getFirstName(), c.getLastName());
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    @ResponseBody 
    public Customer loadCustomerById(@PathVariable("customerId") long customerId) {
        return this.customerService.getCustomerById(customerId);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseBody
    public Customer addCustomer(@RequestBody Customer customer) {
        return this.customerService.createCustomer(customer.getFirstName(), customer.getLastName());
    }

}
