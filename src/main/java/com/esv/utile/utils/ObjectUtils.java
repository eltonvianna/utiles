/*

 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 03/10/2017
 */
public final class ObjectUtils {

    /**
     * <p>Suppress default constructor for non instantiability</p>
     */
    public ObjectUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }
    
    /**
     * @param obj
     * @param message
     */
    public static void requireNotNull(final Object obj, final String message) {
        if (null == obj) {
           throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * @param classesNames the {@link Class} canonical names
     * @param initargs the constructor class arguments
     * @return a new instance of respective given {@link Class}
     * throws {@link IllegalArgumentException} if className parameter is empty
     * throws {@link RuntimeException} caused by caught an {@link Exception} if lenient is <code>false</code>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> newInstances(final String[] classesNames, final Object... initargs) {
        return Arrays.stream(classesNames).map(h -> (T) ObjectUtils.newInstance(h, initargs)).collect(Collectors.toList());
    }
    
    /**
     * @param className the {@link Class} canonical name
     * @param initargs the constructor class arguments
     * @return a new instance of respective given {@link Class}
     * throws {@link IllegalArgumentException} if className parameter is empty
     * throws {@link RuntimeException} caused by caught an {@link Exception} if lenient is <code>false</code>
     */
    public static <T> T newInstance(final String className, final Object... initargs) {
        return ObjectUtils.newInstance(className, false, initargs);
    }

    /**
     * @param clazz the {@link Class} type
     * @param initargs the constructor class arguments
     * @return a new instance of respective given {@link Class}
     * throws {@link IllegalArgumentException} if className parameter is empty
     * throws {@link RuntimeException} caused by caught an {@link Exception} if lenient is <code>false</code>
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... initargs) {
        return ObjectUtils.newInstance(clazz, false, initargs);
    }
    
    /**
     * @param className the {@link Class} canonical name
     * @param initargs the constructor class arguments
     * @param lenient set <code>true</code> to returns <code>null</code> if caught an {@link Exception}
     * @return a new instance of respective given {@link Class}
     * throws {@link IllegalArgumentException} if className parameter is empty
     * throws {@link RuntimeException} caused by caught an {@link Exception} if lenient is <code>false</code>
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className, final boolean lenient,
            final Object... initargs) {
        try {
            return (T) newInstance(Class.forName(className), lenient, initargs);
        } catch (ClassNotFoundException e) {
            try {
                return (T) newInstance(Thread.currentThread().getContextClassLoader().loadClass(className), lenient, initargs);
            } catch (ClassNotFoundException e2) {
            }
            if (false == lenient) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
    /**
     * @param clazz the {@link Class} type
     * @param initargs the constructor class arguments
     * @param lenient set <code>true</code> to returns <code>null</code> if caught an {@link Exception}
     * @return a new instance of respective given {@link Class}
     * throws {@link IllegalArgumentException} if className parameter is empty
     * throws {@link RuntimeException} caused by caught an {@link Exception} if lenient is <code>false</code>
     */
    public static <T> T newInstance(final Class<T> clazz, final boolean lenient,
            final Object... initargs) {
        try {
            final Constructor<T> constructor = ObjectUtils.findConstructor(clazz, initargs);
            constructor.setAccessible(true);
            return constructor.newInstance(initargs);
        } catch (Exception e) {
            if (false == lenient) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    /**
     * @param className
     * @param initargs
     * @return
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findConstructor(final String className, final Object... initargs)
            throws NoSuchMethodException, ClassNotFoundException {
        CharSequenceUtils.requireNotBlank(className, "className parameter is null");
        return (Constructor<T>) ObjectUtils.findConstructor(Class.forName(className), initargs);
    }
    
    /**
     * @param clazz
     * @param initargs
     * @return
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findConstructor(final Class<T> clazz, final Object... initargs)
            throws NoSuchMethodException, ClassNotFoundException {
        ObjectUtils.requireNotNull(clazz, "clazz parameter is null");
        Constructor<T> constructor = null;
        final Class<?>[] types = ObjectUtils.getTypes(initargs);
        try {
            constructor = clazz.getDeclaredConstructor(types);
        } catch (Exception e) {
            findConstructor:
            for (final Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
                final Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
                if (types.length != parameterTypes.length) {
                    continue;
                }
                checkConstructor:
                for (int i = 0; i++ < types.length;) {
                    if (!parameterTypes[i].isAssignableFrom(types[i])) {
                        break checkConstructor;
                    }
                    constructor = (Constructor<T>) declaredConstructor;
                    break findConstructor;
                }
            }
            if (null == constructor) {
                throw new NoSuchMethodException(clazz + "(" + Arrays.toString(types) + ")");
            }
        }
        return constructor;
    }
    
    /**
     * @param initargs
     * @return
     */
    public static Class<?>[] getTypes(final Object... initargs) {
        if (null == initargs || initargs.length == 0) {
            return null;
        }
        return Arrays.stream(initargs).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]);
    }

    /**
     * @param m
     * @return
     */
    public static String canonicalMethotName(final Method m) {
        return m.getDeclaringClass().getCanonicalName() + "#" + m.getName();
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isMap(final Object obj) {
        return null != obj && obj instanceof Map;
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isCollection(final Object obj) {
        return null != obj && obj instanceof Collection;
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isArray(final Object obj) {
        return null != obj && obj.getClass().isArray();
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isEnum(final Object obj) {
        return null != obj && obj.getClass().isEnum();
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isCharSequence(final Object obj) {
        return null != obj && obj instanceof CharSequence || obj instanceof Character;
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isNumber(final Object obj) {
        return null != obj && obj instanceof Number;
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isBoolean(final Object obj) {
        return null != obj && obj instanceof Boolean;
    }
    
    /**
     * <p>Check if is a scalar object, it means, an object with a single value</p>
     * 
     * @param obj
     * @return true if is an instance of {@link CharSequence} or {@link Number} or {@link Boolean}
     */
    public static boolean isScalar(final Object obj) {
        return ObjectUtils.isCharSequence(obj) || ObjectUtils.isNumber(obj) || ObjectUtils.isBoolean(obj);
    }
    
    /**
     * @param obj
     * @return
     */
    public static boolean isNotScalar(final Object obj) {
        return ! ObjectUtils.isScalar(obj);
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static <T> Class<T> getGenericType(final Object obj) {
        return ObjectUtils.getGenericType(obj, 0);
    }
    
    /**
     * @param obj
     * @param typeIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericType(final Object obj, final int typeIndex) {
        if (null == obj) {
            return null;
        }
        return (Class<T>) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[typeIndex];
    }

    /**
     * @param enumType
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<?>> E getEnum(final E enumType, final String name) {
        CharSequenceUtils.requireNotBlank(name, "name parameter is null");
        return null == enumType ? null : (E) Enum.valueOf(enumType.getClass(), name.trim().toUpperCase());
    }
}