/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 28/09/2017
 */
public final class JsonUtils {
    
    private final Map<String, String> builder;
    
    /**
     * 
     */
    private JsonUtils(final String key, final String value) {
        this.builder = new LinkedHashMap<>();
        this.builder.put(key, value);
    }
    
    /**
     * @param obj
     * @return
     */
    private static void mapToJson(final StringBuilder sb, final Object obj) {
        final Map<?, ?> map = (Map<?, ?>) obj;
        if (map.isEmpty()) {
            sb.append(JsonUtils.empty());
            return;
        }
        sb.append("{");
        int counter = 0;
        for (final Entry<?, ?> entry : map.entrySet()) {
            final Object key = entry.getKey();
            if (ObjectUtils.isNotScalar(key)) {
                throw new UnsupportedOperationException("Unsupported non scalar attribute type: " + key.getClass());
            }
            JsonUtils.addAttribute(sb, key);
            JsonUtils.addValue(sb, entry.getValue(), counter, map.size() - 1);
            counter++;
        }
        sb.append("}");
    }

    /**
     * @param sb
     * @param entry
     */
    private static void addAttribute(final StringBuilder sb, final Object obj) {
        sb.append("\"");
        sb.append(String.valueOf(obj));
        sb.append("\":");
    }

    /**
     * 
     * @param sb
     * @param obj
     * @param counter
     * @param lastIndex
     * @return
     */
    private static void addValue(final StringBuilder sb, final Object obj, final int counter, final int lastIndex) {
        if (ObjectUtils.isScalar(obj)) {
            sb.append("\"");
            sb.append(String.valueOf(obj));
            sb.append("\"");
        } else {
            JsonUtils.marshall(sb, obj);
        }
        if (counter < lastIndex) {
            sb.append(",");
        }
    }

    /**
     * 
     * @param collection
     * @return
     */
    private static void collectionToJson(final StringBuilder sb, final Object o) {
        final Collection<?> collection = (Collection<?>) o;
        sb.append("[");
        try {
            if (collection.isEmpty()) {
                return;
            }
            int counter = 0;
            for (final Object obj : collection) {
                JsonUtils.addValue(sb, obj, counter, collection.size() - 1);
                counter++;
            }
        } finally {
            sb.append("]");
        }
    }

    /**
     * 
     * @param array
     * @return
     */
    private static void arrayToJson(final StringBuilder sb, final Object o) {
        final Object[] array = (Object[]) o;
        sb.append("[");
        try {
            if (array.length == 0) {
                return;
            }
            int counter = 0;
            for (final Object obj : array) {
                JsonUtils.addValue(sb, obj, counter, array.length - 1);
                counter++;
            }
        } finally {
            sb.append("]");
        }
    }
    
    /**
     * 
     * @param sb
     * @param obj
     * @return
     */
    private static String marshall(final StringBuilder sb, final Object obj) {
        if (ObjectUtils.isMap(obj)) {
            JsonUtils.mapToJson(sb, obj);
        } else if (ObjectUtils.isArray(obj)) {
            JsonUtils.arrayToJson(sb,obj);
        } else if (ObjectUtils.isCollection(obj)) {
            JsonUtils.collectionToJson(sb,obj);
        } else {
            throw new UnsupportedOperationException("Unsupported object type: " + obj.getClass());
        }
        return sb.toString();
    }
    
    /**
     * @param obj
     * @return
     */
    public static String marshall(final Object obj) {
        if (null == obj) {
            return null;
        }
        return JsonUtils.marshall(new StringBuilder(), obj);
    }

    /**
     * @return
     */
    public static String empty() {
        return "{}";
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public static JsonUtils createBuilder(final String key, final String value) {
        return new JsonUtils(key, value);
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public JsonUtils add(final String key, final String value) {
        this.builder.put(key, value);
        return this;
    }
    
    /**
     * 
     * @return
     */
    public String build() {
        return JsonUtils.marshall(this.builder);
    }
}