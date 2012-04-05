package org.springsource.examples.sawt.web.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;
import org.springsource.examples.sawt.web.gwt.client.entities.CustomerDto;
import org.springsource.examples.sawt.web.gwt.client.service.GwtCustomerService;

@SuppressWarnings("serial,unchecked")
public class GwtCustomerServiceImpl extends RemoteServiceServlet implements GwtCustomerService {
    private CustomerDto fromCustomer(Customer c) {
        try {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerDto, c);
            return customerDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T beanOfType(Class t) {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        return (T) applicationContext.getBean(t);
    }

    public void updateCustomer(long cid, String f, String l) {
        try {
            CustomerService customerService = beanOfType(CustomerService.class);
            customerService.updateCustomer(cid, f, l);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public CustomerDto getCustomerById(long customerId) {
        try {
            CustomerService customerService = beanOfType(CustomerService.class);
            return fromCustomer(customerService.getCustomerById(customerId));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public CustomerDto createCustomer(String f, String ln) {
        try {
            CustomerService customerService = beanOfType(CustomerService.class);
            return fromCustomer(customerService.createCustomer(f, ln));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
