package com.joshlong.binding.android;

import android.widget.TextView;

public class TextViewPropertyValueSource extends BaseViewPropertyValueSource<String, TextView> {
    @Override
    public String getValue() {
        TextView textView = getView();
        return "" + (textView.getText());
    }
}
