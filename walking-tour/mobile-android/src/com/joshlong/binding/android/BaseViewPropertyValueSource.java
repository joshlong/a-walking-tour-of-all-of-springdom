package com.joshlong.binding.android;

import android.view.View;
import com.joshlong.binding.PropertyValueSource;

abstract public class BaseViewPropertyValueSource <T, V extends View>  implements PropertyValueSource<T>{
    public V view ;

    public V getView(){
        return this.view ;
    }
    public BaseViewPropertyValueSource(){}
    public BaseViewPropertyValueSource(V view) {
        this.view = view ;
    }

    @Override
    abstract public T getValue()  ;
}
