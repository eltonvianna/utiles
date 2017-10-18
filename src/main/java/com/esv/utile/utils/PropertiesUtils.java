package com.esv.utile.utils;
/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.esv.utile.logging.Logger;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
public final class PropertiesUtils {

    public static final String DEFAULT_APPLICATION_PROPERTIES = "application.properties";
    
    private static final Map<String, String> properties = new ConcurrentHashMap<>();
    private static final String propertiesFile = "application.propertiesFile";
    private static final AtomicBoolean loadingProperties = new AtomicBoolean(false);
    
    /**
     * @throws IOException
     */
    public static void loadProperties() throws IOException {
        PropertiesUtils.loadProperties(System.getProperty(propertiesFile, PropertiesUtils.DEFAULT_APPLICATION_PROPERTIES));
    }

    /**
     * 
     * @param fileNames
     * @throws IOException
     */
    public static void loadProperties(final String... fileNames) throws IOException {
        if (null == fileNames || fileNames.length == 0) {
            return;
        }
        for (final String fileName : fileNames) {
            try (final InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName)) {
                PropertiesUtils.loadProperties(inputStream);
            }
        }
    }

    /**
     * @param inputStream
     * @throws IOException
     */
    public synchronized static void loadProperties(final InputStream inputStream) throws IOException {
        if (null != inputStream && false == loadingProperties.get()) {
            loadingProperties.set(true);
            try {
                final Properties props = new Properties();
                props.load(inputStream);
                properties.putAll(props.entrySet().stream()
                        .collect(Collectors.toConcurrentMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
            } finally {
                Logger.getLogger().trace(() -> "Configuration load successfully: " + properties);
                loadingProperties.set(false);
            }
        }
    }

    /**
     * Suppressing default constructor for non instantiability
     */
    private PropertiesUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }

    /**
     * @return the applicationPropeties
     */
    public static Map<String, String> getApplicationPropeties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Create a new property whose value is a string and subject to change on-the-fly.
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     */
    public static String getStringProperty(final String propName, final String defaultValue) {
        return PropertiesUtils.getProperty(String::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is an integer and subject to change on-the-fly.
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     */
    public static Integer getIntProperty(final String propName, final int defaultValue) {
        return PropertiesUtils.getProperty(Integer::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a long and subject to change on-the-fly.
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     */
    public static Long getLongProperty(final String propName, final long defaultValue) {
        return PropertiesUtils.getProperty(Long::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a boolean and subject to change on-the-fly..
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     * 
     */
    public static Boolean getBooleanProperty(final String propName, final boolean defaultValue) {
        return PropertiesUtils.getProperty(Boolean::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a float and subject to change on-the-fly.
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     */
    public static Float getFloatProperty(final String propName, final float defaultValue) {
        return PropertiesUtils.getProperty(Float::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a double and subject to change on-the-fly.
     *
     * @param propName property name
     * @param defaultValue default value if the property is not defined in underlying configuration
     */
    public static Double getDoubleProperty(String propName, double defaultValue) {
        return PropertiesUtils.getProperty(Double::valueOf, propName, defaultValue);
    }

    /**
     * @param propName
     * @param defaultValue
     * @param sufixes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(final String propName, final T defaultValue, final String... sufixes) {
        CharSequenceUtils.requireNotBlank(propName, "propName parameter is null");
        ObjectUtils.requireNotNull(defaultValue, "defaultValue parameter is null");
        Object value = null;
        final String property = PropertiesUtils.findPropertyName(propName);
        switch (defaultValue.getClass().getCanonicalName()) {
        case "java.lang.String":
            value = PropertiesUtils.getProperty(String::valueOf, property, defaultValue);
            break;
        case "int":
        case "java.lang.Integer":
            value = PropertiesUtils.getProperty(Integer::valueOf, property, defaultValue);
            break;
        case "long":
        case "java.lang.Long":
            value = PropertiesUtils.getProperty(Long::valueOf, property, defaultValue);
            break;
        case "boolean":
        case "java.lang.Boolean":
            value = PropertiesUtils.getProperty(Boolean::valueOf, property, defaultValue);
            break;
        case "float":
        case "java.lang.Float":
            value = PropertiesUtils.getProperty(Float::valueOf, property, defaultValue);
            break;
        case "double":
        case "java.lang.Double":
            value = PropertiesUtils.getProperty(Double::valueOf, property, defaultValue);
            break;
        default:
            if (defaultValue.getClass().isEnum()) {
                value = PropertiesUtils.getEnumProperty(property, (Enum<?>) defaultValue);
                break;
            }
            throw new IllegalArgumentException(
                    "Invalid property value type: " + (null != defaultValue ? defaultValue.getClass() : "null"));
        }
        return (T) value;
    }

    /**
     * @param propName
     * @param sufixes
     * @return
     */
    private static String findPropertyName(final String propName, final String... sufixes) {
        if (null != sufixes && sufixes.length > 0) {
            final List<String> props = Arrays.stream(sufixes).map(s -> propName + "." + s)
                    .filter(s -> properties.containsKey(s)).collect(Collectors.toList());
            if (!props.isEmpty()) {
                return props.get(0);
            }
        }
        return propName;
    }

    /**
     * 
     * @param propName
     * @param defaultValue
     * @return
     */
    public static <E extends Enum<?>> E getEnumProperty(final String propName, final E defaultValue) {
        try {
            final String propValue = PropertiesUtils.getProperty(propName);
            return CharSequenceUtils.isBlank(propValue) ? defaultValue : ObjectUtils.getEnum(defaultValue, propValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param consumer
     * @param propName
     * @param defaultValue
     * @return
     */
    private static <T> T getProperty(final Function<String, T> consumer, final String propName, final T defaultValue) {
        try {
            final String propValue = PropertiesUtils.getProperty(propName);
            return CharSequenceUtils.isBlank(propValue) ? defaultValue : consumer.apply(propValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 
     * @param propName
     * @return
     */
    private static String getProperty(final String propName) {
        if (CharSequenceUtils.isBlank(propName)) {
            return null;
        }
        return properties.getOrDefault(propName, System.getProperty(propName));
    }

    /**
     * @param propName
     * @param value
     */
    public synchronized static <T> void setProperty(final String propName, final T value) {
        if (null == value) {
            return;
        }
        properties.put(propName, String.valueOf(value));
    }

    /**
     * @param value
     * @return
     */
    public static String getRequiredProperty(final String propName) {
        return Optional.ofNullable(PropertiesUtils.getProperty(propName)).orElseThrow(IllegalArgumentException::new);
    }
}