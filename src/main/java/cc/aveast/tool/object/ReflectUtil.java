package cc.aveast.tool.object;

import cc.aveast.common.ErrorCode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ReflectUtil {

    /**
     * 获取List成员变量的泛型类型
     * @param field List成员变量
     * @return 泛型类型
     */
    public static Class getListParameterizedType(Field field) {
        if (field.getType().isAssignableFrom(List.class)) {
            return null;
        }

        return getParameterizedType(field.getGenericType(), 0);
    }

    /**
     * 获取MAP成员变量的key泛型类型
     * @param field MAP成员变量
     * @return Key泛型类型
     */
    public static Class getMapKeyParameterizedType(Field field) {
        if (field.getType().isAssignableFrom(Map.class)) {
            return null;
        }

        return getParameterizedType(field.getGenericType(), 0);
    }

    /**
     * 获取MAP成员变量的value泛型类型
     * @param field MAP成员变量
     * @return Value泛型类型
     */
    public static Class getMapValueParameterizedType(Field field) {
        if (field.getType().isAssignableFrom(Map.class)) {
            return null;
        }

        return getParameterizedType(field.getGenericType(), 1);
    }

    /**
     * 获取[第n]泛型类型
     * @param fieldType 成员变量type
     * @param index 第n
     * @return 泛型类型类
     */
    private static Class getParameterizedType(Type fieldType, int index) {
        if (fieldType == null) {
            return null;
        }

        if (fieldType instanceof ParameterizedType) {
            ParameterizedType resultType = (ParameterizedType) fieldType;
            return (Class) resultType.getActualTypeArguments()[index];
        }

        return null;
    }

    public static String getParamValue(Object obj, String name) {
        Field field;
        String result;

        try {
            field = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }

        field.setAccessible(true);
        result = getFieldValue(field, obj);
        field.setAccessible(false);
        return result;
    }

    public static Object getParamObject(Object obj, String name) {
        Field field;
        Object result;

        try {
            field = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }

        field.setAccessible(true);
        result = getFieldObject(field, obj);
        field.setAccessible(false);
        return result;
    }

    private static Object getFieldObject(Field field, Object object) {
        Class type = field.getType();

        if (type.isPrimitive()) {
            return getPrimitiveObject(type, field, object);
        }

        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static String getFieldValue(Field field, Object object) {
        Class type = field.getType();

        if (type.isPrimitive()) {
            return getPrimitiveValue(type, field, object);
        }

        try {
            if (type.isEnum()) {
                Enum enumEl = (Enum) field.get(object);
                return enumEl.name();
            }

            if (type.isAssignableFrom(String.class)) {
                return (String) field.get(object);
            } else if (Number.class.isAssignableFrom(type)) {
                Number number = (Number) field.get(object);
                if (number == null) {
                    return null;
                }
                return number.toString();
            } else {
                Object tmp = field.get(object);
                return tmp.toString();
            }
        } catch (IllegalAccessException e) {
            return null;
        }
    }


    public static String getPrimitiveValue(Class type, Field field, Object object) {
        try {
            if (type.toString().equals("long")) {
                return String.valueOf(field.getLong(object));
            } else if (type.toString().equals("int")) {
                return String.valueOf(field.getInt(object));
            } else if (type.toString().equals("double")) {
                return String.valueOf(field.getDouble(object));
            } else if (type.toString().equals("float")) {
                return String.valueOf(field.getFloat(object));
            } else if (type.toString().equals("boolean")) {
                return String.valueOf(field.getBoolean(object));
            } else {
                return field.get(object).toString();
            }
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Object getPrimitiveObject(Class type, Field field, Object object) {
        try {
            if (type.toString().equals("long")) {
                return field.getLong(object);
            } else if (type.toString().equals("int")) {
                return field.getInt(object);
            } else if (type.toString().equals("double")) {
                return field.getDouble(object);
            } else if (type.toString().equals("float")) {
                return field.getFloat(object);
            } else if (type.toString().equals("boolean")) {
                return field.getBoolean(object);
            } else {
                return object;
            }
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Object[] getAllParamValue(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object[] result = new Object[fields.length];

        int i = 0;

        for (Field curFld : fields) {
            try {
                boolean access = curFld.isAccessible();
                if (!access) {
                    curFld.setAccessible(true);
                }

                result[i++] = curFld.get(obj);
                if (!access) {
                    curFld.setAccessible(false);
                }
            } catch (Exception ex) {
                return null;
            }
        }

        return result;
    }

    public static String getSqlParams(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        int i = 0;

        for (Field curFld : fields) {
            try {
                boolean access = curFld.isAccessible();
                if (!access) {
                    curFld.setAccessible(true);
                }

                sb.append(curFld.get(obj));
                if (!access) {
                    curFld.setAccessible(false);
                }
                sb.append(",");
            } catch (Exception ex) {
                return null;
            }
        }

        return sb.toString().substring(0, sb.length() - 1);
    }

    public static void setValue(Object object, String key, String value) {
        Field field;
        try {
            field = object.getClass().getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            Class father = object.getClass().getSuperclass();
            if (father != null && !father.toString().endsWith("Object")) {
                try {
                    field = father.getDeclaredField(key);
                } catch (Exception e1) {
                    return;
                }
            } else {
                return;
            }
        }

        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (Exception e) {
            return;
        }
        field.setAccessible(false);
    }


    public static void setValue(Object object, String key, Object value) {
        Field field;
        try {
            field = object.getClass().getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            Class father = object.getClass().getSuperclass();
            if (father != null && !father.toString().endsWith("Object")) {
                try {
                    field = father.getDeclaredField(key);
                } catch (Exception e1) {
                    return;
                }
            } else {
                return;
            }
        }

        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (Exception e) {
            return;
        }
        field.setAccessible(false);
    }

    public static String getValue(Object object, String key) {
        Field field;

        try {
            field = object.getClass().getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            Class father = object.getClass().getSuperclass();
            if (father != null && !father.toString().endsWith("Object")) {
                try {
                    field = father.getDeclaredField(key);
                } catch (Exception e1) {
                    return null;
                }
            } else {
                return null;
            }
        }

        field.setAccessible(true);
        try {
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        } finally {
            field.setAccessible(false);
        }
    }


    /**
     * 对基本类成员变量进行赋值
     *
     * @param fieldType 成员变量类型
     * @param curField  成员变量
     * @param field     字段值
     * @param curObject 当前类
     * @return ErrorCode.PARAM_TYPE_ERROR 类型不支持
     */
    public static int setPrimitiveValue(Class fieldType, Field curField, byte[] field, Object curObject) {
        String type = fieldType.toString();

        try {
            if (type.equals("long")) {
                curField.set(curObject, Long.parseLong(new String(field)));
            } else if (type.equals("int")) {
                curField.set(curObject, Integer.parseInt(new String(field)));
            } else if (type.equals("double")) {
                curField.set(curObject, Double.parseDouble(new String(field)));
            } else if (type.equals("float")) {
                curField.set(curObject, Float.parseFloat(new String(field)));
            } else if (type.equals("boolean")) {
                curField.set(curObject, Boolean.parseBoolean(new String(field)));
            } else {
                curField.set(curObject, new String(field));
            }
        } catch (IllegalAccessException e) {
            return ErrorCode.PARAM_TYPE_ERROR.getCode();
        }

        return 0;
    }

    /**
     * 对String类型变量进行赋值
     * @param curField  成员变量
     * @param field     待赋值
     * @param curObject 当前对象
     * @return ErrorCode.PARAM_TYPE_ERROR 类型不支持
     */
    public static int setStringValue(Field curField, byte[] field, Object curObject) {
        try {
            curField.set(curObject, new String(field).trim());
        } catch (IllegalAccessException e) {
            return ErrorCode.PARAM_TYPE_ERROR.getCode();
        }

        return 0;
    }

}
