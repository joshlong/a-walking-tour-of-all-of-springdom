package org.springsource.examples.sawt.web.android.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * wrapper object for a collection of {@link Customer} entities
 */

@Root(name = "customers")
public class CustomerList {

    @ElementList(inline = true, required = false)
    private List<Customer> customers = new ArrayList<Customer>();

    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * no-op for serialization
     */
    public CustomerList() {
    }

    public void setCustomers(Collection<Customer> c) {
        if (this.customers != null)
            this.customers.clear();
        else
            this.customers = new ArrayList<Customer>();

        if (c != null)
            this.customers.addAll(c);
    }

    public CustomerList(Collection<Customer> c) {
        setCustomers(c);
    }

}
