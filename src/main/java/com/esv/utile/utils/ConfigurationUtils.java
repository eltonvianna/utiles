/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 19/09/2017
 */
public final class ConfigurationUtils {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final Map<String, String> APPLICATION_PROPETIES;
    private static final String CONFIGURATION_FILE = "config.file";

    static {
        try {
            APPLICATION_PROPETIES = new ConcurrentHashMap<>(ConfigurationUtils.loadConfiguration());
            LOGGER.finest(() -> "Current application properties: " + APPLICATION_PROPETIES);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Suppressing default constructor for non instantiability
     */
    private ConfigurationUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }

    /**
     * @throws IOException
     */
    private static Map<String, String> loadConfiguration() throws IOException {
        final String filename = System.getProperty(CONFIGURATION_FILE, "application.properties");
        try (final InputStream inputStream = ConfigurationUtils.class.getClassLoader().getResourceAsStream(filename)) {
            final byte[] configData = IOUtils.toByteArray(inputStream);
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(configData));
            final ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.FINE);
            LOGGER.addHandler(consoleHandler);
            LOGGER.setLevel(Level.FINE);
            LOGGER.setUseParentHandlers(false);
            LOGGER.config(() -> "Logging successfully configured...");
            final Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(configData));
            LOGGER.config(() -> "Application properties successfully loaded...");
            return properties.entrySet().stream()
                    .collect(Collectors.toMap(p -> p.getKey().toString(), p -> p.getValue().toString()));
        }
    }

    /**
     * @return the applicationPropeties
     */
    public static Map<String, String> getApplicationPropeties() {
        return Collections.unmodifiableMap(APPLICATION_PROPETIES);
    }

    /**
     * Create a new property whose value is a string and subject to change
     * on-the-fly.
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static String getStringProperty(final String propName, final String defaultValue) {
        return ConfigurationUtils.getProperty(String::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is an integer and subject to change
     * on-the-fly..
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static Integer getIntProperty(final String propName, final int defaultValue) {
        return ConfigurationUtils.getProperty(Integer::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a long and subject to change
     * on-the-fly..
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static Long getLongProperty(final String propName, final long defaultValue) {
        return ConfigurationUtils.getProperty(Long::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a boolean and subject to change
     * on-the-fly..
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static Boolean getBooleanProperty(final String propName, final boolean defaultValue) {
        return ConfigurationUtils.getProperty(Boolean::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a float and subject to change
     * on-the-fly..
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static Float getFloatProperty(final String propName, final float defaultValue) {
        return ConfigurationUtils.getProperty(Float::valueOf, propName, defaultValue);
    }

    /**
     * Create a new property whose value is a double and subject to change
     * on-the-fly..
     *
     * @param propName
     *            property name
     * @param defaultValue
     *            default value if the property is not defined in underlying
     *            configuration
     */
    public static Double getDoubleProperty(String propName, double defaultValue) {
        return ConfigurationUtils.getProperty(Double::valueOf, propName, defaultValue);
    }
    
    /**
     * @param consumer
     * @param propName
     * @param defaultValue
     * @return
     */
    private static <T> T getProperty(final Function<String, T> consumer, final String propName, final T defaultValue) {
        try {
            final String propValue = APPLICATION_PROPETIES.get(propName);
            return CharSequenceUtils.isBlank(propValue) ? defaultValue : consumer.apply(propValue);
        } catch (Exception e) {
            LOGGER.warning(() -> "Returning default value: " + defaultValue + " for property: " + propName + ". Cause: " + e.getMessage());
            return defaultValue;
        }
    }

    /**
     * @param propName
     * @param value
     */
    public static void setProperty(final String propName, final String value) {
        APPLICATION_PROPETIES.put(propName, value);
    }

    /**
     * @param value
     * @return
     */
    public static String getRequiredProperty(final String propName) {
        return Optional.ofNullable(APPLICATION_PROPETIES.get(propName)).orElseThrow(IllegalArgumentException::new);
    }
}