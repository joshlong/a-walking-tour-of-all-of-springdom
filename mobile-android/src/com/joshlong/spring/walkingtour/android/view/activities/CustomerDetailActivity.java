package com.joshlong.spring.walkingtour.android.view.activities;

import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.R;
import com.joshlong.spring.walkingtour.android.model.Customer;
import com.joshlong.spring.walkingtour.android.service.CustomerService;
import com.joshlong.spring.walkingtour.android.view.activities.support.AbstractActivity;

import javax.inject.Inject;

/**
 * Handles displaying the fields for an individual {@link com.joshlong.spring.walkingtour.android.model.Customer}
 *
 * @author Josh Long
 */
public class CustomerDetailActivity extends AbstractActivity {

    // model information
    @Inject
    CustomerService customerService;
    Customer customer;
    Long customerId;
    // references to components
    EditText firstNameEditText, lastNameEditText;
    Button saveButton;
    // background worker async task

    void loadCustomer(final Long customerId) {
        AsyncTask<Void, Void, Customer> fetchCustomerAsyncTask = new AsyncTask<Void, Void, Customer>() {
            @Override
            protected Customer doInBackground(Void... params) {
                try {
                    return customerService.getCustomerById(customerId);
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.getLocalizedMessage(), e);
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(Customer c) {
                CustomerDetailActivity.this.customer = c;
                CustomerDetailActivity.this.customerId = customer.getId();
                firstNameEditText.setText(c.getFirstName());
                lastNameEditText.setText(c.getLastName());
            }
        };
        fetchCustomerAsyncTask.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCustomer(this.customerId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.customerId = getIntent().getExtras().getLong("customerId");

        setContentView(R.layout.customer_detail_activity);

        this.firstNameEditText = (EditText) findViewById(R.id.first_name);
        this.lastNameEditText = (EditText) findViewById(R.id.last_name);
        this.saveButton = (Button) findViewById(R.id.save_button);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncTask<Customer, Void, Customer> saveCustomerAsyncTask = new AsyncTask<Customer, Void, Customer>() {
                        @Override
                        protected Customer doInBackground(Customer... params) {
                            Customer response = customerService.updateCustomer(
                                    customerId,
                                    firstNameEditText.getText().toString(),
                                    lastNameEditText.getText().toString());
                            return response;
                        }
                    };
                    Customer result = saveCustomerAsyncTask.execute().get();
                    Toast.makeText(CustomerDetailActivity.this, "your changes to record #" + result.getId() + " have been saved", Toast.LENGTH_SHORT).show();
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        });
    }

}