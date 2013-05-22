package com.joshlong.spring.walkingtour.android.view.activities;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.R;
import com.joshlong.spring.walkingtour.android.model.Customer;
import com.joshlong.spring.walkingtour.android.service.CustomerService;
import com.joshlong.spring.walkingtour.android.utils.DaggerInjectionUtils;
import com.joshlong.spring.walkingtour.android.view.activities.support.*;
import com.joshlong.spring.walkingtour.android.view.support.AbstractListAdapter;

import javax.inject.Inject;
import java.util.*;

/**
 * This is the first {@link Activity } displayed to the user.
 *
 * @author Josh Long
 */
public class CustomerListActivity extends AbstractAsyncListActivity implements AsyncActivity {

    List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
    @Inject
    CustomerService customerService;

    protected void loadCustomers() {
        FetchCustomersAsyncTask fetchCustomersAsyncTask = new FetchCustomersAsyncTask();
        AsyncTask<Void, Void, List<Customer>> results = fetchCustomersAsyncTask.execute();
        try {
            List<Customer> customers = results.get();
            this.customers.clear();
            this.customers.addAll(customers);
            CustomerListAdapter customerListAdapter = new CustomerListAdapter(this, customers);
            setListAdapter(customerListAdapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerInjectionUtils.inject(this);

        int mainActivityResourceId = R.layout.customer_list_activity;
        setContentView(mainActivityResourceId);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Customer customer = customers.get(position);
        Intent intent = new Intent(this, CustomerDetailActivity.class);
        intent.putExtra("customerId", customer.getId());    // if this is present, then the activity will act as an editor.
        startActivity(intent);
    }

    class CustomerListAdapter extends AbstractListAdapter<Customer> {
        private Context context;

        public CustomerListAdapter(Context context, List<Customer> rows) {
            super(rows);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            Customer customer = getItem(position);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.customer_list_item, parent, false);
            }
            if (customer != null) {
                ((TextView) convertView.findViewById(R.id.id)).setText(Long.toString(customer.getId()));
                ((TextView) convertView.findViewById(R.id.name)).setText(customer.getFirstName() + ' ' + customer.getLastName());
            }
            return convertView;
        }

    }

    class FetchCustomersAsyncTask extends AsyncTask<Void, Void, List<Customer>> {

        private String fetchingMessage =
                getString(R.string.fetching);

        @Override
        protected void onPreExecute() {
            showProgressDialog(fetchingMessage);
        }

        @Override
        protected List<Customer> doInBackground(Void... params) {
            try {
                return customerService.loadAllCustomers();
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getLocalizedMessage(), e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<Customer> customerList) {
            dismissProgressDialog();
        }
    }


}

/**
 * @author Roy Clarkson
 */
  /*  public class TwitterProfileListAdapter extends BaseAdapter {
        private TwitterProfile twitterProfile;
        private final LayoutInflater layoutInflater;

        public TwitterProfileListAdapter(Context context, TwitterProfile twitterProfile) {
            if (twitterProfile == null) {
                throw new IllegalArgumentException("twitterProfile cannot be null");
            }

            this.twitterProfile = twitterProfile;
            this.layoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return 5;
        }

        public String[] getItem(int position) {
            String[] item = new String[2];

            switch (position) {
                case 0:
                    item[0] = "Id";
                    item[1] = String.valueOf(twitterProfile.getId());
                    break;
                case 1:
                    item[0] = "Screen Name";
                    item[1] = twitterProfile.getScreenName();
                    break;
                case 2:
                    item[0] = "Name";
                    item[1] = twitterProfile.getName();
                    break;
                case 3:
                    item[0] = "Description";
                    item[1] = twitterProfile.getDescription();
                    break;
                case 4:
                    item[0] = "Created Date";
                    item[1] = twitterProfile.getCreatedDate() == null ? "" : twitterProfile.getCreatedDate().toString();
                    break;
                default:
                    item[0] = "";
                    item[1] = "";
                    break;
            }

            return item;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            String[] item = getItem(position);

            View view = convertView;

            if (view == null) {
                view = layoutInflater.inflate(android.R.layout.two_line_list_item, parent, false);
            }

            TextView t = (TextView) view.findViewById(android.R.id.text1);
            t.setText(item[0]);

            t = (TextView) view.findViewById(android.R.id.text2);
            t.setText(item[1]);

            return view;
        }

    } */