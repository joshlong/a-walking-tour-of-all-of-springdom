package com.joshlong.spring.walkingtour.android.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.*;
import com.joshlong.spring.walkingtour.android.config.InjectCrm;
import com.joshlong.spring.walkingtour.android.service.*;
import com.squareup.otto.*;
import org.springsource.examples.sawt.web.android.R;

import javax.inject.Inject;

/**
 * Android UI designed to support editing a single {@link com.joshlong.spring.walkingtour.android.Customer} entity.
 */
public class EditCustomerFormActivity extends CrmBaseActivity {

    @Inject
    @InjectCrm
    Crm crm;
    @Inject
    CustomerService customerService;
    @Inject
    AndroidStringUtils androidStringUtils;
    Button saveCustomer;
    String baseServiceUrl;
    TextView customerIdTextField;
    EditText firstNameTextField, lastNameTextField;
    View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            save();
        }
    };
    boolean editing = false;
    long customerId;

    @Subscribe
    public void onDeadEvent(DeadEvent deadEvent) {
        Log.w(getClass().getName(), "received a dead event " + deadEvent.getClass() + " deadEvent(" + deadEvent + ") with payload object " + deadEvent.event + ".");
    }

    @Subscribe
    public void onCustomerEvent(CustomerEvent customerEvent) {
        loadCustomerDataIntoForm(customerEvent.getCustomer());
    }

    private String safeString(String i) {
        return i == null || i.trim().length() == 0 ? "" : i;
    }

    protected void loadCustomerDataIntoForm(Customer c) {
        this.customerId = c.getId();
        this.editing = this.customerId > 0;
        this.customerIdTextField.setText((c.getId() > 0) ? "#" + c.getId() + "" : getString(R.string.new_customer));
        this.firstNameTextField.setText(safeString(c.getFirstName()));
        this.lastNameTextField.setText(safeString(c.getLastName()));
    }

    protected void save() {

        String firstName = androidStringUtils.stringValueFor(firstNameTextField),
                lastName = androidStringUtils.stringValueFor(lastNameTextField);

        if (editing) {
            customerService.updateCustomer(customerId, firstName, lastName);
        } else {
            Customer customer = this.customerService.createCustomer(firstName, lastName);
            this.loadCustomerDataIntoForm(customer);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // todo from the first intent to the second and also find out about retroactive consumers.

        setContentView(R.layout.edit_customer);

        this.baseServiceUrl = getString(R.string.base_uri);
        this.customerService = new CustomerServiceClient(this.baseServiceUrl);

        // load UI components
        customerIdTextField = (TextView) this.findViewById(R.id.customer_id_label);
        firstNameTextField = (EditText) this.findViewById(R.id.edit_first_name);
        lastNameTextField = (EditText) this.findViewById(R.id.edit_last_name);

        saveCustomer = (Button) this.findViewById(R.id.save_customer);
        saveCustomer.setOnClickListener(this.saveOnClickListener);

        Bundle bundle = getIntent().getExtras(); // todo figure out how to communicate the customer
        long customerId = bundle.getLong("customerId");
        if(customerId > 0)
        loadCustomerDataIntoForm(customerService.getCustomerById(customerId));
    }

   /* @Override
    protected void onResume() {
        super.onResume();
      loadCustomerDataIntoForm(  crm.getCustomer());
    }*/
}
