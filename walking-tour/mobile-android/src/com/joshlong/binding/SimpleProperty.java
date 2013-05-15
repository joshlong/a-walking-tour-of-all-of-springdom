package com.joshlong.binding;


public abstract class SimpleProperty<T> implements Property<T> {

    private String propertyName;

    public SimpleProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    public abstract T getValue();

    @Override
    public PropertyValueSource<T> getPropertyValueSource() {
        return new PropertyValueSource<T>() {
            @Override
            public T getValue() {
                return SimpleProperty.this.getValue();
            }
        };
    }
}
