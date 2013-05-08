package org.springsource.examples.sawt.web.android;

import android.text.Editable;
import android.widget.EditText;

public class Utils {

    public static String stringValueFor(Editable editable) {
        char[] cs = new char[editable.length()];
        editable.getChars(0, editable.length(), cs, 0);
        return new String(cs);
    }

    public static String stringValueFor(EditText editable) {
        return stringValueFor(editable.getText());
    }
}
