package com.joshlong.spring.walkingtour.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.*;
import org.springframework.util.Assert;
import org.springsource.examples.sawt.web.android.*;
import com.joshlong.spring.walkingtour.android.model.Customer;
import com.joshlong.spring.walkingtour.android.service.*;

public class Welcome extends Activity {

    private Button newBtn;
    private Button editBtn;
    private EditText customerIdText;
    private View.OnClickListener editCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String customerIdStr = Utils.stringValueFor(Welcome.this.customerIdText);
            Assert.hasText(customerIdStr);
            Long id = Long.parseLong(customerIdStr);
            CustomerService customerServiceClient = crmApplication().getCustomerService();
            Customer c = customerServiceClient.getCustomerById(id);
            if (c == null) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.customer_could_not_be_found), Toast.LENGTH_SHORT);
                toast.show();
            } else {
                showEditorFor(c);
            }
        }
    };
    private View.OnClickListener newCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showEditorFor(new Customer());
        }
    };

    private void showEditorFor(Customer c) {
        CrmApplication.crmApplicationInstance(this).setCustomer(c);
        Intent i = new Intent(this, EditCustomerForm.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        newBtn = (Button) findViewById(R.id.new_customer_btn);
        newBtn.setOnClickListener(newCustomerBtn);

        customerIdText = (EditText) findViewById(R.id.cid);
        editBtn = (Button) findViewById(R.id.customer_load_button);
        editBtn.setOnClickListener(editCustomerBtn);
    }

    protected CrmApplication crmApplication() {
        return CrmApplication.crmApplicationInstance(this);
    }
}
