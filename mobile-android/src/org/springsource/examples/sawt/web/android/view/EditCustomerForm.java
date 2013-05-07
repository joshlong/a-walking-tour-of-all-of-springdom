package org.springsource.examples.sawt.web.android.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.springframework.util.StringUtils;
import org.springsource.examples.sawt.web.android.CrmApplication;
import org.springsource.examples.sawt.web.android.R;
import org.springsource.examples.sawt.web.android.Utils;
import org.springsource.examples.sawt.web.android.model.Customer;
import org.springsource.examples.sawt.web.android.service.CustomerService;
import org.springsource.examples.sawt.web.android.service.CustomerServiceClient;

/**
 * Android UI designed to support editing a single {@link org.springsource.examples.sawt.web.android.model.Customer} entity.
 */
public class EditCustomerForm extends Activity implements View.OnClickListener {

    private CustomerService customerService;
    private Button saveCustomer;
    private String baseServiceUrl;
    private TextView customerId;
    private EditText firstNameTextField, lastNameTextField;
    private Customer customer;


    public void onClick(View view) {
        syncCustomerEdits();
    }


    private void setCustomer(Customer c) {
        this.customerId.setText((c.getId() > 0) ? "#" + c.getId() + "" : getString(R.string.new_customer));
        this.firstNameTextField.setText((StringUtils.hasText(c.getFirstName())) ? c.getFirstName() : "");
        this.lastNameTextField.setText((StringUtils.hasText(c.getLastName())) ? c.getLastName() : "");
        this.customer = c;
    }


    private void syncCustomerEdits() {

        if (this.customer != null) {
            this.customer.setFirstName(Utils.stringValueFor(firstNameTextField));
            this.customer.setLastName(Utils.stringValueFor(lastNameTextField));
            if (this.customer.getId() > 0)
                this.customerService.updateCustomer(this.customer.getId(), customer.getFirstName(), customer.getLastName());
            else {
                CrmApplication crmApplication = CrmApplication.crmApplicationInstance(this);
                Customer customer = this.customerService.createCustomer(this.customer.getFirstName(), this.customer.getLastName());
                crmApplication.setCustomer(customer);
                setCustomer(crmApplication.getCustomer());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Customer customerToEdit = CrmApplication.crmApplicationInstance(this).getCustomer();
        setCustomer(customerToEdit);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer);

        this.baseServiceUrl = getString(R.string.base_uri);
        this.customerService = new CustomerServiceClient(this.baseServiceUrl);

        // load UI components
        customerId = (TextView) this.findViewById(R.id.customer_id_label);
        firstNameTextField = (EditText) this.findViewById(R.id.edit_first_name);
        lastNameTextField = (EditText) this.findViewById(R.id.edit_last_name);

        saveCustomer = (Button) this.findViewById(R.id.save_customer);
        saveCustomer.setOnClickListener(this);

    }

}
