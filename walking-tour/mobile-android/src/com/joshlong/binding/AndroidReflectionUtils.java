package com.joshlong.binding;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.*;

class AndroidReflectionUtils {

    Map<String, Method> methods(Set<Class<?>> classes, final MethodKeyCreator keyCreator,
                                ReflectionUtils.MethodFilter methodFilter) {
        final Map<String, Method> methods = new HashMap<String, Method>();
        for (Class<?> c : classes)
            ReflectionUtils.doWithMethods(c, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    methods.put(keyCreator.keyFrom(method), method);
                }
            }, methodFilter);
        return methods;
    }

    public Map<String, Method> gettersFrom(Set<Class<?>> classes) {
        return this.methods(classes, new MethodKeyCreator() {
                    @Override
                    public String keyFrom(Method obj) {
                        return propertyFromSetterMethodName(obj);
                    }
                }, new ReflectionUtils.MethodFilter() {
                    @Override
                    public boolean matches(Method method) {
                        return isGetter(method) && !method.getName().equals("getClass");
                    }
                }
        );

    }

    public Map<String, Method> settersFrom(Set<Class<?>> classes) {
        Map<String, Method> setters = new HashMap<String, Method>();
        for (Class<?> potentialClass : classes) {
            Method[] methods = potentialClass.getMethods();
            for (Method m : methods) {
                if (isSetter(m)) {
                    setters.put(propertyFromSetterMethodName(m), m);
                }
            }
        }
        return setters;
    }

    public boolean isGetter(Method m) {
        return isPropertyMethod(m) && m.getName().startsWith("get");
    }

    private boolean isPropertyMethod(Method method) {
        String methodName = method.getName();
        boolean isPublic = Modifier.isPublic(method.getModifiers());
        char startOfNoun = methodName.charAt(3);
        boolean isSetter = (Character.isUpperCase(startOfNoun) || !Character.isLetter(startOfNoun));
        return isPublic && isSetter;
    }

    public boolean isSetter(Method m) {
        return isPropertyMethod(m) && m.getName().startsWith("set");
    }

    public String setterFromPropertyName(String propertyName) {
        return "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    }

    private String propertyFromSetterMethodName(Method method) {
        String propertyName = method.getName();
        String noun = propertyName.substring(3);
        noun = Character.toLowerCase(noun.charAt(0)) + noun.substring(1);
        return noun;
    }

    private static interface MethodKeyCreator {
        String keyFrom(Method obj);
    }
}