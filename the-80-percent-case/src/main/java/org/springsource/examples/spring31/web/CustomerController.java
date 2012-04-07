package org.springsource.examples.spring31.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springsource.examples.spring31.services.Customer;
import org.springsource.examples.spring31.services.CustomerService;

@RequestMapping("/crm/")
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Customer customerById(@PathVariable("id") Integer id) {
        return this.customerService.getCustomerById(id);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Customer> customers() {
        return this.customerService.getAllCustomers();
    }

    @RequestMapping(value = "/customers", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Customer addCustomer(@Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Integer updateCustomer(@PathVariable("id") Integer id, @RequestBody Customer customer) {
        customerService.updateCustomer(id, customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
        return id;
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Integer deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        return id;
    }

}             
