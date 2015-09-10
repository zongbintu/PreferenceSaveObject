package com.tu.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Preference工具类 Created by atlas on 15/4/25. Email:atlas.tufei@gmail.com
 */
public class PreferenceUtils {
    public final static String TAG = PreferenceUtils.class.getSimpleName();

    private PreferenceUtils() {
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putString(key, value).commit();
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defValue);
    }

    public static boolean setValue(Context context, String key, float value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putFloat(key, value).commit();
    }

    public static float getValue(Context context, String key, float defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getFloat(key, defValue);
    }

    public static boolean setValue(Context context, String key, double value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putLong(key, Double.doubleToLongBits(value)).commit();
    }

    public static double getValue(Context context, String key, double defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(defValue)));
    }

    /**
     * @param context
     * @param name    Desired preferences file. If a preferences file by this name
     *                does not exist, it will be created when you retrieve an editor
     * @param key
     * @param value
     * @return
     */
    public static boolean setValue(Context context, String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putString(key, value).commit();
    }

    public static String getValue(Context context, String name, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putFloat(key, value).commit();
    }

    public static float getValue(Context context, String name, String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static boolean setValue(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String name, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.edit().putInt(key, value).commit();
    }

    public static int getValue(Context context, String name, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static boolean setValue(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putInt(key, value).commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defValue);
    }

    public static boolean setValue(Context context, String key, long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putLong(key, value).commit();
    }

    public static long getValue(Context context, String key, long defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 是否 Parceable CREATOR
     *
     * @param field
     * @return
     */
    private static boolean isParcelableCreator(Field field) {
        return Modifier.toString(field.getModifiers()).equals("public static final") && "CREATOR".equals(field.getName());
    }

    /**
     * 存储Object,支持简单类型
     *
     * @param context
     * @param o
     * @return
     */
    public static boolean setObject(Context context, Object o) {
        // 获取所有成员
        Field[] fields = o.getClass().getDeclaredFields();
        SharedPreferences sp = context.getSharedPreferences(o.getClass().getName(), Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        for (int i = 0; i < fields.length; i++) {
            if (isParcelableCreator(fields[i]))
                continue;
            Class<?> type = fields[i].getType();
            final String name = fields[i].getName();
            if (isSingle(type)) {
                try {
                    if (type == Character.TYPE || type.equals(String.class)) {
                        Object value = fields[i].get(o);
                        editor.putString(name, null == value ? null : value.toString());
                    } else if (type.equals(int.class) || type.equals(Short.class))
                        editor.putInt(name, fields[i].getInt(o));
                    else if (type.equals(double.class))
                        editor.putLong(name, Double.doubleToLongBits(fields[i].getDouble(o)));
                    else if (type.equals(float.class))
                        editor.putFloat(name, fields[i].getFloat(o));
                    else if (type.equals(long.class) && // 不将序列化后的字段加入
                            !name.equals("serialVersionUID"))
                        editor.putLong(name, fields[i].getLong(o));
                    else if (type.equals(boolean.class))
                        editor.putBoolean(name, fields[i].getBoolean(o));

                } catch (IllegalAccessException e) {
                    LogUtils.e(TAG, e);
                } catch (IllegalArgumentException e) {
                    LogUtils.e(TAG, e);
                }
            } else {
                if (isObject(type)) {
                    if (type.equals(Date.class)) {
                        try {
                            editor.putLong(name, ((Date) (fields[i].get(o))).getTime());
                        } catch (IllegalAccessException e) {
                            LogUtils.e(TAG, e);
                        }
                        continue;
                    }
                    // 对象写入
                    try {
                        Object temp = fields[i].get(o);
                        if (null != temp)
                            setObject(context, temp);
                        else {
                            try {
                                setObject(context, fields[i].getClass().newInstance());
                            } catch (InstantiationException e) {
                            }
                        }
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                } else {
                    // write other type
                    try {
                        Object temp = fields[i].get(o);
                        if (null != temp)
                            editor.putString(name, new GsonBuilder().create().toJson(temp));
                    } catch (IllegalAccessException e) {
                        LogUtils.e(TAG, e);
                    }
                }
            }
        }

        return editor.commit();
    }

    /**
     * 存储Object,支持简单类型
     *
     * @param <T>
     * @param context
     * @param clazz
     * @return
     */
    public static <T> T getObject(Context context, Class<T> clazz) {
        T o = null;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException e) {
            LogUtils.e(TAG, e);
            return o;
        } catch (IllegalAccessException e) {
            LogUtils.e(TAG, e);
            return o;
        }
        Field[] fields = clazz.getDeclaredFields();
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        for (int i = 0; i < fields.length; i++) {
            if (isParcelableCreator(fields[i]))
                continue;
            Class<?> type = fields[i].getType();
            final String name = fields[i].getName();
            if (isSingle(type)) {
                try {
                    fields[i].setAccessible(true);
                    if (type == Character.TYPE || type.equals(String.class)) {
                        final String value = sp.getString(name, null);
                        if (null != value)
                            fields[i].set(o, value);
                    } else if (type.equals(int.class) || type.equals(Short.class))
                        fields[i].setInt(o, sp.getInt(name, 0));
                    else if (type.equals(double.class))
                        fields[i].setDouble(o, Double.longBitsToDouble(sp.getLong(name, 0)));
                    else if (type.equals(float.class))
                        fields[i].setFloat(o, sp.getFloat(name, 0));
                    else if (type.equals(long.class))
                        fields[i].setLong(o, sp.getLong(name, 0));
                    else if (type.equals(boolean.class))
                        fields[i].setBoolean(o, sp.getBoolean(name, false));
                } catch (IllegalAccessException e) {
                    LogUtils.e(TAG, e);
                } catch (IllegalArgumentException e) {
                    LogUtils.e(TAG, e);
                }
            } else {
                if (isObject(type)) {
                    if (type.equals(Date.class)) {
                        try {
                            fields[i].set(o, new Date(sp.getLong(name, 0)));
                        } catch (IllegalAccessException e) {
                            LogUtils.e(TAG, e);
                        }
                        continue;
                    }

                    // 对象写入
                    Object temp = getObject(context, fields[i].getType());
                    if (null != temp) {
                        fields[i].setAccessible(true);
                        try {
                            fields[i].set(o, temp);
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        }
                    }
                } else {
                    // read other object
                    String tempValue = sp.getString(name, null);
                    if (!TextUtils.isEmpty(tempValue)) {
                        Object tempObj = null;
                        tempObj = new GsonBuilder().create().fromJson(tempValue, type);
                        if (null != tempObj) {
                            fields[i].setAccessible(true);
                            try {
                                fields[i].set(o, tempObj);
                            } catch (IllegalAccessException e) {
                                LogUtils.e(TAG, e);
                            }
                        }
                    }
                }
            }
        }

        return o;
    }

    /**
     * 判断是否是对象 *
     */
    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) && !isMap(clazz);
    }

    /**
     * 判断是否是值类型 *
     */
    private static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    /**
     * 是否布尔值 *
     */
    public static boolean isBoolean(Class<?> clazz) {
        return (clazz != null) && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class.isAssignableFrom(clazz)));
    }

    /**
     * 是否数值 *
     */
    public static boolean isNumber(Class<?> clazz) {
        return (clazz != null)
                && ((Byte.TYPE.isAssignableFrom(clazz)) || (Short.TYPE.isAssignableFrom(clazz)) || (Integer.TYPE.isAssignableFrom(clazz)) || (Long.TYPE.isAssignableFrom(clazz))
                || (Float.TYPE.isAssignableFrom(clazz)) || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class.isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是字符串 *
     */
    public static boolean isString(Class<?> clazz) {
        return (clazz != null) && ((String.class.isAssignableFrom(clazz)) || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class.isAssignableFrom(clazz)));
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

    public static void clearSettings(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().commit();
    }

    public static void clearSettings(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * clear object
     *
     * @param context
     * @param clazz
     */
    public static void clearObject(Context context, Class clazz) {
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            Class<?> type = fields[i].getType();
            if (isObject(type))
                clearObject(context, type);
        }
        clearSettings(context, clazz.getName());
    }
}