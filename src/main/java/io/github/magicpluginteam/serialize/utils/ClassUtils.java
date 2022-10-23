package io.github.magicpluginteam.serialize.utils;

import java.lang.reflect.ParameterizedType;

public class ClassUtils {

    public static Class<?> getGenericType(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
