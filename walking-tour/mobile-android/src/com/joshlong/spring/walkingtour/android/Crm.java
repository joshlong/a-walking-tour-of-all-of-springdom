package com.joshlong.spring.walkingtour.android;


import android.app.Application;
import com.joshlong.spring.walkingtour.android.config.CrmModule;
import dagger.ObjectGraph;

import java.util.*;

public class Crm extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        Object[] modules = getModules().toArray();
        objectGraph = ObjectGraph.create(modules);
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new CrmModule(this)
        );
    }

    public ObjectGraph getObjectGraph() {
        return this.objectGraph;
    }


}
