package com.joshlong.spring.walkingtour.mobileweb;

import com.joshlong.spring.walkingtour.services.*;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@Controller
@RequestMapping
public class RestCustomerController {

    @Autowired
    private SearchCapableCustomerService customerService;

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

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseBody
    public CustomerList customers(@RequestParam(value = "query", required = false) String q) {
        Collection<Customer> c = StringUtils.isEmpty(q) ? this.customerService.loadAllCustomers() : this.customerService.search(q);
        return new CustomerList(c );
    }

    public static class CustomerList extends ArrayList<Customer> {
        public CustomerList(Collection<? extends Customer> c) {
            super(c);
        }
    }


}
