package com.joshlong.spring.walkingtour.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.*;
import com.joshlong.spring.walkingtour.android.service.CustomerService;
import com.squareup.otto.*;
import org.springframework.util.Assert;
import org.springsource.examples.sawt.web.android.R;

import javax.inject.Inject;


public class WelcomeActivity extends CrmBaseActivity {
    @Inject AndroidStringUtils androidStringUtils;
    @Inject CustomerService customerService;

    Button newBtn;
    Button editBtn;
    EditText customerIdText;

    View.OnClickListener editCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        String customerIdStr = androidStringUtils.stringValueFor(WelcomeActivity.this.customerIdText);
        Assert.hasText(customerIdStr);
        Long id = Long.parseLong(customerIdStr);
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            loadCustomer(customer);
        } else {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.customer_could_not_be_found),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        }
    };

    View.OnClickListener newCustomerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadCustomer(new Customer());
        }
    };

    @Subscribe
    public void onCustomerEvent(CustomerEvent customerEvent ){
         Log.d(getClass().getName(),  "customerEvent " +customerEvent.getCustomer());
    }

    void loadCustomer(Customer customer) {
        getBus().post(new CustomerEvent(customer));
        Intent intent = new Intent(this, EditCustomerFormActivity.class);

        startActivity(intent);  // http://www.vogella.com/articles/AndroidIntent/article.html#intentfilter_browser


    }

    @Subscribe
    public void onDeadEvent(DeadEvent deadEvent) {
        Log.w(getClass().getName(), "received a dead event " + deadEvent.getClass() + " deadEvent(" + deadEvent + ")");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        (newBtn = (Button) findViewById(R.id.new_customer_btn)).setOnClickListener(newCustomerBtn);

        customerIdText = (EditText) findViewById(R.id.cid);

        (editBtn = (Button) findViewById(R.id.customer_load_button)).setOnClickListener(editCustomerBtn);
    }


}
