package org.springsource.examples.sawt.web.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.springframework.util.Assert;
import org.springsource.examples.sawt.web.android.CrmApplication;
import org.springsource.examples.sawt.web.android.R;
import org.springsource.examples.sawt.web.android.Utils;
import org.springsource.examples.sawt.web.android.model.Customer;

public class Welcome extends Activity {

    private Button newBtn;
    private Button editBtn;
    private EditText customerIdText;

    private void showEditorFor(Customer c) {
        CrmApplication.crmApplicationInstance(this).setCustomer(c);
        Intent i = new Intent(this, EditCustomerForm.class);
        startActivity(i);
    }

    private View.OnClickListener editCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String customerIdStr = Utils.stringValueFor(Welcome.this.customerIdText);
            Assert.hasText(customerIdStr);
            Long id = Long.parseLong(customerIdStr);
            Customer c = CrmApplication.crmApplicationInstance(Welcome.this).getCustomerService().getCustomerById(id);
            showEditorFor(c);
        }
    };

    private View.OnClickListener newCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showEditorFor(new Customer());
        }
    };

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
}
