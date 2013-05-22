package com.joshlong.spring.walkingtour.android.async;

import android.os.AsyncTask;

import java.lang.reflect.*;


/**
 * builds up a proxy
 *
 * @author Josh Long
 */
public class RunOnIoThreadProxyCreator {

    public static <T> T runOnIoThread(final T o) {
        return runOnIoThread(o, o.getClass().getInterfaces());
    }

    static Object safeInvoke(Object target, Method method, Object[] arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (Throwable thro) {
            throw new RuntimeException(thro);
        }
    }

    /**
     * this will get run on the native UI thread, by default. Since we'll always be running inside of some Activity
     */
    @SuppressWarnings("unchecked")
    public static <T> T runOnIoThread(
            final T target,
            final Class<?>... tClass) {

        InvocationHandler invocationHandler = new InvocationHandler() {

            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

                Class<RunOnIoThread> classOfAnnotation = RunOnIoThread.class;
                final Method methodToInvokeOnTargetObject = target.getClass().getMethod(method.getName(), method.getParameterTypes());
                final boolean runInIoThread = method.getAnnotation(classOfAnnotation) != null || methodToInvokeOnTargetObject.getAnnotation(classOfAnnotation) != null;

                // stash a reference to the Activity that we're currently in
                if (runInIoThread) {
                    AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {
                        private Object result;
                        private AsyncCallback savedAsyncCallback;

                        @Override
                        protected Object doInBackground(Object... params) {
                            AsyncCallback<Object> asyncUiCallback = new AsyncCallback<Object>() {
                                @Override
                                public void methodInvocationCompleted(Object o) {
                                    result = o;
                                }
                            };
                            int asyncCallbackArgumentIndex = 0;
                            for (Object arg : args) {
                                if (arg instanceof AsyncCallback) {
                                    savedAsyncCallback = (AsyncCallback) arg;
                                    args[asyncCallbackArgumentIndex] = asyncUiCallback;
                                    break;
                                }
                                asyncCallbackArgumentIndex += 1;
                            }
                            return safeInvoke(target, methodToInvokeOnTargetObject, args);

                        }

                        /**
                         * deliver the result of the invocation to any AsyncCallback arguments in the method
                         * invocation.
                         *
                         * @param o the result of the processing in {@link #doInBackground(Object...)} call.
                         *
                         */
                        @Override
                        protected void onPostExecute(Object o) {
                            // either the method arguments contained an AsyncCallbacka argument, in which case, were good now.
                            if ( this.savedAsyncCallback != null && result != null) { // this is the use case where we have
                             this.savedAsyncCallback.methodInvocationCompleted(result);
                            } // or the method arguments contained no AsyncCallbacks and probably even had a return value

                            // otherwise, we do nothing here.
                        }
                    };

                    asyncTask.execute();

                    return null;
                }
                // otherwise simply invoke the method as usual
                return safeInvoke(target, methodToInvokeOnTargetObject, args);
            }
        };


        Object objectProxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), tClass, invocationHandler);
        return (T) objectProxy;
    }

    private static AsyncCallback<?> findCallbackFor(Object[] arguments) throws Throwable {
        for (Object obj : arguments) {
            if (obj instanceof AsyncCallback) {
                return (AsyncCallback) obj;
            }
        }
        return null;
    }
}
