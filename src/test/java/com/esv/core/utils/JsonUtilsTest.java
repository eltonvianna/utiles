/* 
 * © 2017 Springer Nature 
 */
package com.esv.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.JsonUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 11/10/2017
 */
public class JsonUtilsTest {

    @Test
    public void testMarshallMapObject() {
        final Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr1", "String");
        map.put("attr2", 1);
        map.put("attr3", 3.14d);
        map.put("attr4", 'x');
        map.put("attr5", true);
        Assert.assertEquals("{\"attr1\":\"String\",\"attr2\":\"1\",\"attr3\":\"3.14\",\"attr4\":\"x\",\"attr5\":\"true\"}", JsonUtils.marshall(map));
    }
    
    @Test
    public void testMarshallMapObjectWithVector() {
        final List<Object> list = Arrays.asList("A", "B", "C", "D", "E");
        final Object[] array = new Object[] {'a', 'b', 'c', 'd'};
        final Collection<Object> collection = new TreeSet<>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);
        collection.add(4);
        final Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr1", list);
        map.put("attr2", array);
        map.put("attr3", collection);
        Assert.assertEquals("{\"attr1\":[\"A\",\"B\",\"C\",\"D\",\"E\"],\"attr2\":[\"a\",\"b\",\"c\",\"d\"],\"attr3\":[\"1\",\"2\",\"3\",\"4\"]}", JsonUtils.marshall(map));
    }
    
    @Test
    public void testMarshallMapObjectWithVectorWithMap() {
        final Object[] array = new Object[] {'a', 'b', 'c', 'd'};
        final Map<String,Object> listMap = new LinkedHashMap<>();
        listMap.put("attr4", "String");
        listMap.put("attr5", 1);
        listMap.put("attr6", array);
        final List<Object> list = Arrays.asList("A", listMap, "C", "D", "E");
        final Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr1", list);
        Assert.assertEquals("{\"attr1\":[\"A\",{\"attr4\":\"String\",\"attr5\":\"1\",\"attr6\":[\"a\",\"b\",\"c\",\"d\"]},\"C\",\"D\",\"E\"]}", JsonUtils.marshall(map));
    }
    
    @Test
    public void testMarshallListObjectWithMap() {
        final Object[] array = new Object[] {'a', 'b', 'c', 'd'};
        final Map<String,Object> map1 = new LinkedHashMap<>();
        map1.put("attr1", "String");
        map1.put("attr2", 1);
        map1.put("attr3", array);
        final Map<String,Object> map3 = new LinkedHashMap<>();
        map3.put("attr6", "test");
        map3.put("attr7", 0.1d);
        map3.put("attr8", '0');
        final Map<String,Object> map2 = new LinkedHashMap<>();
        map2.put("attr4", true);
        map2.put("attr5", Arrays.asList("1", 2, 3, 4d));
        map2.put("map3", map3);
        final List<Object> list = Arrays.asList("map1", map1, "map2", map2);
        Assert.assertEquals("[\"map1\",{\"attr1\":\"String\",\"attr2\":\"1\",\"attr3\":[\"a\",\"b\",\"c\",\"d\"]},\"map2\",{\"attr4\":\"true\",\"attr5\":[\"1\",\"2\",\"3\",\"4.0\"],\"map3\":{\"attr6\":\"test\",\"attr7\":\"0.1\",\"attr8\":\"0\"}}]", JsonUtils.marshall(list));
    }
}
