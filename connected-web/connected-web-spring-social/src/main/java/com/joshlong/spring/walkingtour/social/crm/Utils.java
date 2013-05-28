package com.joshlong.spring.walkingtour.social.crm;


import java.io.*;

class Utils {
    public static void closeQuietly(Reader reader) {
        if (null != reader) {
            try {
                reader.close();
            } catch (Throwable th) {/*don't care*/}
        }
    }

    public static void closeQuietly(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Throwable th) {/*don't care*/}
        }

    }

    public static void log(String msg, Object... pars) {
        System.out.println(String.format(msg, pars));
    }
}