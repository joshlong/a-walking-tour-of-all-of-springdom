package com.joshlong.binding;

public   interface Property<T> {
    String getPropertyName();

    PropertyValueSource<T> getPropertyValueSource();
}
