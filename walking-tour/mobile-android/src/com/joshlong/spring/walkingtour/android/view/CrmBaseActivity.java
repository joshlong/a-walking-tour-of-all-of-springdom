package com.joshlong.spring.walkingtour.android.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.joshlong.spring.walkingtour.android.Crm;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * base class to override to include Dagger dependency injection.
 *
 * @author Joshua Long
 */
public class CrmBaseActivity extends Activity {
    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crm crm = (Crm) getApplication();
        Log.w(getClass().getName(), "about to objectGraph.inject( " + getClass().getName() + ") in #onCreate(Bundle)");
        crm.getObjectGraph().inject(this);
        Log.w(getClass().getName(), "finished objectGraph.inject( " + getClass().getName() + ") in #onCreate(Bundle)");

        Log.w(getClass().getName(), "about to register " + getClass().getName() + " on bus");
        this.bus.register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(getClass().getName(), "about to register " + getClass().getName() + " on bus");
        this.bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(getClass().getName(), "about to un-register " + getClass().getName() + " on bus");
        this.getBus().unregister(this);
    }

    public Bus getBus() {
        Log.w(getClass().getName(), "returning cached instance of " + this.bus.toString() + ".");
        return this.bus;
    }
}
