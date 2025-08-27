package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Injector {
  public static <T> T injectDependencies(T instance) throws IllegalAccessException, InvocationTargetException {
    for (Field field : instance.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Inject.class)) {
        Object dependency = ComponentRegistrator.createComponent(field.getType());
        field.setAccessible(true);
        field.set(instance, dependency);
      }
    }

    for (Method method : instance.getClass().getDeclaredMethods()) {
      if (method.isAnnotationPresent(Inject.class)) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
          args[i] = ComponentRegistrator.createComponent(paramTypes[i]);
        }
        method.setAccessible(true);
        method.invoke(instance, args);
      }
    }

    return (ClassScanner) instance;
  }
}
