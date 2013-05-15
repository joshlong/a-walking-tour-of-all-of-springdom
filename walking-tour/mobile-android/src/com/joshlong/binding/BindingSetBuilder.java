package com.joshlong.binding;

import java.lang.reflect.Method;
import java.util.*;

public class BindingSetBuilder {
    private List<Property<?>> propertyList = new ArrayList<Property<?>>();
    private AndroidReflectionUtils androidReflectionUtils = new AndroidReflectionUtils();
    private BindingSet bindingSet = new BindingSet() {
        @Override
        public List<Property<?>> getProperties() {
            return propertyList;
        }
    };

    public <T> BindingSetBuilder bindBean(final Object obj) {
        Set<Class<?>> objClasses = new HashSet<Class<?>>();
        objClasses.add(obj.getClass());
        for (Class<?> c : objClasses)
            objClasses.add(c);

        final Map<String, Method> stringMethodMap = this.androidReflectionUtils.gettersFrom(objClasses);

        for (String propertyName : stringMethodMap.keySet())
            this.propertyList.add(new GetterMethodInvokingProperty(obj, propertyName, stringMethodMap));

        return this;

    }

    public <T> BindingSetBuilder bind(String propertyName, final Object value) {
        this.propertyList.add(new SimpleProperty<Object>(propertyName) {
            @Override
            public Object getValue() {
                return value;
            }
        });
        return this;
    }

    public <T> BindingSetBuilder bind(String propertyName, final PropertyValueSource<T> pvs) {
        this.propertyList.add(new SimpleProperty<T>(propertyName) {

            @Override
            public T getValue() {
                return pvs.getValue();
            }
        });
        return this;
    }

    public BindingSet buildBindingSet() {
        return bindingSet;
    }

    public static class GetterMethodInvokingProperty extends SimpleProperty<Object> {

        private Object target;
        private Map<String, Method> methods;

        public GetterMethodInvokingProperty(Object target, String propertyName, Map<String, Method> methods) {
            super(propertyName);
            this.target = target;
            this.methods = methods;
        }

        @Override
        public Object getValue() {
            Method readerMethod = this.methods.get(this.getPropertyName());
            try {
                return readerMethod.invoke(this.target);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

}
