package org.springsource.examples.sawt.web.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.springsource.examples.sawt.web.gwt.client.entities.CustomerDto;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("crm")
public interface GwtCustomerService extends RemoteService {
    void updateCustomer(long cid, String f, String l);

    CustomerDto getCustomerById(long customerId);

    CustomerDto createCustomer(String f, String ln);
}
