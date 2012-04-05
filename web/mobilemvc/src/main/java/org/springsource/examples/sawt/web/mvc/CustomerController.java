package org.springsource.examples.sawt.web.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomerController {


    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private CustomerService customerService;

    @ModelAttribute  // so that Spring MVC has something to bind form values to
    public Customer customer() {
        return new Customer();
    }

    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public String  customer(@RequestParam("id") Long id, Model model ) {
        Customer customerById = this.customerService.getCustomerById(id);
        model.addAttribute( "customer", customerById);
        return "customers/display";
    }


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void setupAddition() {
        // demonstrates that this is a front controller
        // the interesting 'setup' is being done by the model ('customer')
        log.info("About to show the add page, which will be 'add.jsp'");
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddition(@ModelAttribute Customer c, Model modelAndView) {
        log.debug(String.format("adding %s, %s", c.getFirstName(), c.getLastName()));
        Customer customer = this.customerService.createCustomer(c.getFirstName(), c.getLastName());
        modelAndView.addAttribute("id", customer.getId());
        return "redirect:/display";
    }


}


