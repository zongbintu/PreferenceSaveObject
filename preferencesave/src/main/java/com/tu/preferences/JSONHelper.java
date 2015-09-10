package com.tu.preferences;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON序列化辅助类
 *
 * @author Atlas
 *         Email:atlas.tufei@gmail.com
 * @date 2014年9月2日
 */
public class JSONHelper {
    private static final String TAG = JSONHelper.class.getSimpleName();

    /**
     * 将对象转换成Json字符串
     *
     * @param obj
     * @return
     */
    public static String serialize(Object obj) {
        JSONStringer js = new JSONStringer();
        serialize(js, obj);
        return js.toString();
    }

    /**
     * 序列化为JSON *
     */
    private static void serialize(JSONStringer js, Object o) {
        if (isNull(o)) {
            try {
                js.value(null);
            } catch (JSONException e) {
                LogUtils.e(TAG, e);
            }
            return;
        }

        Class<?> clazz = o.getClass();
        if (isObject(clazz)) { // 对象
            serializeObject(js, o);
        } else if (isArray(clazz)) { // 数组
            serializeArray(js, o);
        } else if (isCollection(clazz)) { // 集合
            Collection<?> collection = (Collection<?>) o;
            serializeCollect(js, collection);
        } else if (isMap(clazz)) { // 集合
            HashMap<?, ?> collection = (HashMap<?, ?>) o;
            serializeMap(js, collection);
        } else { // 单个值
            try {
                js.value(o);
            } catch (JSONException e) {
                LogUtils.e(TAG, e);
            }
        }
    }

    /**
     * 序列化数组 *
     */
    private static void serializeArray(JSONStringer js, Object array) {
        try {
            js.array();
            for (int i = 0; i < Array.getLength(array); ++i) {
                Object o = Array.get(array, i);
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
    }

    /**
     * 序列化集合 *
     */
    private static void serializeCollect(JSONStringer js,
                                         Collection<?> collection) {
        try {
            js.array();
            for (Object o : collection) {
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
    }

    /**
     * 序列化对象
     * <p/>
     * *
     */
    private static void serializeObject(JSONStringer js, Object obj) {
        try {
            js.object();
            for (Field f : obj.getClass().getDeclaredFields()) {
                // 不将序列化后的字段加入
                if (!f.getName().equals("serialVersionUID")) {
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    js.key(f.getName());
                    serialize(js, o);
                }
            }
            js.endObject();
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
    }

    /**
     * 序列化Map
     *
     * @param js  json对象
     * @param map map对象
     */
    private static void serializeMap(JSONStringer js, Map<?, ?> map) {
        try {
            js.object();
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) map;
            Iterator<Map.Entry<String, Object>> it = valueMap.entrySet()
                    .iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
                        .next();
                Object key = entry.getKey();
                if (isSingle(key.getClass()))
                    js.key(String.valueOf(entry.getKey()));
//                else if(isObject())
                //TODO map
                serialize(js, entry.getValue());
            }
            js.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化简单对象
     *
     * @throws
     */
    public static <T> T parseObject(JSONObject jo, Class<T> clazz) {
        if (clazz == null || isNull(jo)) {
            return null;
        }

        T obj = createInstance(clazz);
        if (obj == null) {
            return null;
        }
        // 解决不能set父类属性值的问题
        Class forClazz = obj.getClass();
        for (; forClazz != Object.class; forClazz = forClazz.getSuperclass()) {
            for (Field f : forClazz.getDeclaredFields()) {
                if (!"CREATOR".equals(f.getName())) {
                    f.setAccessible(true);
                    setField(obj, f, jo);
                }
            }
        }

        return obj;
    }

    /**
     * 反序列化简单对象
     *
     * @throws
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (clazz == null || jsonString == null || jsonString.length() == 0) {
            return null;
        }
        JSONObject jo = null;
        try {
            jo = new JSONObject(jsonString);
        } catch (JSONException e) {
            LogUtils.e(TAG, e);
        }

        if (isNull(jo)) {
            return null;
        }

        return parseObject(jo, clazz);
    }

    /**
     * 反序列化数组对象
     *
     * @throws
     */
    public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
        if (clazz == null || isNull(ja)) {
            return null;
        }

        int len = ja.length();

        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, len);

        for (int i = 0; i < len; ++i) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                T o = parseObject(jo, clazz);
                array[i] = o;
            } catch (JSONException e) {
                LogUtils.e(TAG, e);
            }
        }

        return array;
    }

    /**
     * 反序列化数组对象
     *
     * @throws
     */
    public static <T> T[] parseArray(String jsonString, Class<T> clazz) {
        if (clazz == null || jsonString == null || jsonString.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(jsonString);
        } catch (JSONException e) {
            LogUtils.e(TAG, e);
        }

        if (isNull(jo)) {
            return null;
        }

        return parseArray(jo, clazz);
    }

    /**
     * 反序列化泛型集合
     *
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> parseCollection(JSONArray ja,
                                                    Class<?> collectionClazz, Class<T> genericType) {

        if (collectionClazz == null || genericType == null || isNull(ja)) {
            return null;
        }

        if (collectionClazz == List.class)
            collectionClazz = ArrayList.class;

        Collection<T> collection = (Collection<T>) createInstance(collectionClazz);
        T o;
        for (int i = 0; i < ja.length(); ++i) {
            try {
                if (isSingle(genericType)) { // 值类型
                    o = (T) ja.get(i);
                } else {
                    JSONObject jo = ja.getJSONObject(i);
                    o = parseObject(jo, genericType);
                }
                collection.add(o);
            } catch (JSONException e) {
                LogUtils.e(TAG, e);
            }
        }

        return collection;
    }
    public static <K,V> Map<K,V> parseMap(JSONStringer ja,
                                                    Class<?> clazz) {

        if (clazz == null || isNull(ja)) {
            return null;
        }

        Map<K,V> map = (Map<K,V>) createInstance(clazz);
        K o;
        //TODO 未完成
//        for (int i = 0; i < ja.length(); ++i) {
//            try {
//                if (isSingle(genericType)) { // 值类型
//                    o = (T) ja.get(i);
//                } else {
//                    JSONObject jo = ja.getJSONObject(i);
//                    o = parseObject(jo, genericType);
//                }
//                collection.add(o);
//            } catch (JSONException e) {
//                LogUtils.e(TAG, e);
//            }
//        }

        return map;
    }

    /**
     * get generic from field
     *
     * @param field
     * @return
     */
    public static Class getGeneric(Field field) {
        Class<?> c = null;
        Type gType = field.getGenericType();
        if (gType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) gType;
            Type[] targs = ptype.getActualTypeArguments();
            if (targs != null && targs.length > 0) {
                Type t = targs[0];
                c = (Class<?>) t;
            }
        }
        return c;
    }

    /**
     * 反序列化泛型集合
     *
     * @throws
     */
    public static <T> Collection<T> parseCollection(String jsonString,
                                                    Class<?> collectionClazz, Class<T> genericType) {
        if (collectionClazz == null || genericType == null
                || jsonString == null || jsonString.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isNull(jo)) {
            return null;
        }

        return parseCollection(jo, collectionClazz, genericType);
    }

    /**
     * 根据类型创建对象 *
     */
    private static <T> T createInstance(Class<T> clazz) {
        if (clazz == null)
            return null;
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
        return obj;
    }

    /**
     * 设定字段的值 *
     */
    private static void setField(Object obj, Field f, JSONObject jo) {

        String name = f.getName();
        Class<?> clazz = f.getType();
        // LogUtils.d(TAG, "name:" + name + "   clazz:" + clazz);
        try {
            if (isArray(clazz)) { // 数组
                Class<?> c = clazz.getComponentType();
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object array = parseArray(ja, c);
                    f.set(obj, array);
                }
            } else if (isCollection(clazz)) { // 泛型集合
                // 获取定义的泛型类型
                Class<?> c = getGeneric(f);

                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object o = parseCollection(ja, clazz, c);
                    f.set(obj, o);
                }
            } else if (isSingle(clazz)) { // 值类型
                if (f.getType() == Character.TYPE) {
                    String str = jo.optString(name);
                    if (str == null || "".equals(str)) {
                        return;
                    }
                    f.setChar(obj, str.toCharArray()[0]);
                } else if (f.getType().equals(int.class)) {
                    int o = jo.optInt(name);

                    f.setInt(obj, o);
                } else if (clazz.equals(double.class)) {
                    double o = jo.optDouble(name);
                    if (Double.isNaN(o)) {
                        f.setDouble(obj, 0.00);
                    } else {
                        f.setDouble(obj, o);
                    }

                } else if (clazz.equals(float.class)) {
                    String o = jo.optString(name);

                    if ("null".equals(o) || TextUtils.isEmpty(o)
                            || "NULL".equals(o)) {
                        f.setFloat(obj, 0);
                    } else
                        f.setFloat(obj, Float.valueOf(o));
                } else if (clazz.equals(String.class)) {
                    String o = jo.optString(name);
                    // TODO 为解决订餐APP中服务器端字符串返回null的情况
                    o = "null".equals(o) ? "" : o;
                    f.set(obj, o);
                } else if (clazz.equals(long.class)) {
                    long o = jo.optLong(name);
                    f.set(obj, o);
                } else {
                    Object o = jo.opt(name);
                    if (o != null) {
                        f.set(obj, o);
                    }
                }
            } else if (isObject(clazz)) { // 对象
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    f.set(obj, o);
                }
            } else {
                throw new Exception("unknow type!");
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
    }

    /**
     * 判断对象是否为空 *
     */
    private static boolean isNull(Object obj) {
        if (obj instanceof JSONObject) {
            return JSONObject.NULL.equals(obj);
        }
        return obj == null;
    }

    /**
     * 判断是否是值类型 *
     */
    public static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    /**
     * 是否布尔值 *
     */
    public static boolean isBoolean(Class<?> clazz) {
        return (clazz != null)
                && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 是否数值 *
     */
    public static boolean isNumber(Class<?> clazz) {
        return (clazz != null)
                && ((Byte.TYPE.isAssignableFrom(clazz))
                || (Short.TYPE.isAssignableFrom(clazz))
                || (Integer.TYPE.isAssignableFrom(clazz))
                || (Long.TYPE.isAssignableFrom(clazz))
                || (Float.TYPE.isAssignableFrom(clazz))
                || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是字符串 *
     */
    public static boolean isString(Class<?> clazz) {
        return (clazz != null)
                && ((String.class.isAssignableFrom(clazz))
                || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是对象 *
     */
    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz)
                && !isCollection(clazz) && !isMap(clazz);
    }

    /**
     * 判断是否是数组 *
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * 判断是否是集合 *
     */
    public static boolean isCollection(Class<?> clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是Map
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是列表
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }
}