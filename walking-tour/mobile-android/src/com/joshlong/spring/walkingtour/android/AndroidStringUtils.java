package com.joshlong.spring.walkingtour.android;

import android.text.Editable;
import android.widget.EditText;


public class AndroidStringUtils {

    public AndroidStringUtils() {
    }

    public String stringValueFor(Editable editable) {
        char[] cs = new char[editable.length()];
        editable.getChars(0, editable.length(), cs, 0);
        return new String(cs);
    }

    public String stringValueFor(EditText editable) {
        return stringValueFor(editable.getText());
    }
}
