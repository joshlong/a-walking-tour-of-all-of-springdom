package com.joshlong.binding;

import java.lang.reflect.Method;
import java.util.*;

public class Binder {
    private BindingSet bindingSet;
    private AndroidReflectionUtils androidReflectionUtils = new AndroidReflectionUtils();

    public Binder(BindingSet bindingSet) {
        this.bindingSet = bindingSet;
    }

    public static Binder bind(BindingSet bindingSet) {
        return new Binder(bindingSet);
    }

    public Binder to(Object instance, Class... clazzes) throws Throwable {
        Set<Class<?>> uniqueClasses = new HashSet<Class<?>>();
        uniqueClasses.add(instance.getClass());
        for (Class <?> c : clazzes)
         uniqueClasses.add( c) ;
        for (Class <?> c : instance.getClass().getInterfaces())
          uniqueClasses.add(c);

        Map<String, Method> writers = androidReflectionUtils.settersFrom(uniqueClasses);
        for (String k : writers.keySet())
            System.out.println(k);

        BindingSet bindingSet = this.bindingSet;
        List<Property<?>> properties = bindingSet.getProperties();
        for (Property<?> bp : properties) {
            String propertyName = bp.getPropertyName();
            PropertyValueSource pvs = bp.getPropertyValueSource();
            Method writerMethod = writers.get(propertyName);
            writerMethod.invoke(instance, pvs.getValue());
        }
        return this;
    }

}
