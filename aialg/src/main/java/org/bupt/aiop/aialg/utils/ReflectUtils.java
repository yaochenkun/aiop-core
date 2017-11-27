package org.bupt.aiop.aialg.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 反射工具类
 */
public class ReflectUtils {

    public static final Class<?>[] EMPTY_PARAM_TYPES = new Class<?>[0];
    public static final Object[] EMPTY_PARAMS = new Object[0];

    /**
     * 从指定类中获取指定字段
     *
     * @param sourceClass         要获取字段的类
     * @param fieldName           字段名
     * @param isFindDeclaredField 是否查找Declared字段
     * @param isUpwardFind        是否在父类中查找字段
     * @return 查找到的字段，如果没有查到返回NULL
     */
    public static Field getField(Class<?> sourceClass, String fieldName, boolean isFindDeclaredField, boolean isUpwardFind) {
        Field field = null;
        try {
            field = isFindDeclaredField ? sourceClass.getDeclaredField(fieldName) : sourceClass.getField(fieldName);
        } catch (NoSuchFieldException e) {
            if (isUpwardFind) {
                Class<?> classes = sourceClass.getSuperclass();
                while (field == null && classes != null) {
                    try {
                        field = isFindDeclaredField ? classes.getDeclaredField(fieldName) : classes.getField(fieldName);
                    } catch (NoSuchFieldException e1) {
                        classes = classes.getSuperclass();
                    }
                }
            }
        }
        return field;
    }

    /**
     * 简化调用参数，默认查找父类和Declared字段
     *
     * @param sourceClass 要查找的类
     * @param fieldName   要查找的字段名
     * @return
     */
    public static Field getField(Class<?> sourceClass, String fieldName) {
        return getField(sourceClass, fieldName, true, true);
    }

    /**
     * 获取给定类的所有字段
     *
     * @param sourceClass         给定的类
     * @param isGetDeclaredField  是否获取Declared类型的字段
     * @param isGetParentField    是否获取父类的字段
     * @param isGetAllParentField 是否获取祖先类的字段
     * @param isDESCGet           在最终获取的列表中，父类的字段是否需要排在子类的前面，只有指定获取父类字段时，该参数才有用
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClass, boolean isGetDeclaredField, boolean isGetParentField,
                                        boolean isGetAllParentField, boolean isDESCGet) {
        List<Field> fieldList = new ArrayList<>();
        if (isGetParentField) {
            List<Class<?>> classList = null;
            if (isGetAllParentField) {
                classList = getSuperClasss(sourceClass, true);
            } else {
                classList = new ArrayList<>();
                classList.add(sourceClass);
                Class<?> superClass = sourceClass.getSuperclass();
                if (superClass != null) {
                    classList.add(superClass);
                }
            }
            if (isDESCGet) {
                for (int w = classList.size() - 1; w > -1; --w) {
                    fieldList.addAll(Arrays.asList(isGetDeclaredField ? classList.get(w).getDeclaredFields() : classList.get(w).getFields()));
                }
            } else {
                for (Class<?> clazz : classList) {
                    fieldList.addAll(Arrays.asList(isGetDeclaredField ? clazz.getDeclaredFields() : clazz.getFields()));
                }
            }
        } else {
            fieldList.addAll(Arrays.asList(isGetDeclaredField ? sourceClass.getDeclaredFields() : sourceClass.getFields()));
        }
        return fieldList;
    }

    /**
     * 获取给定类的所有字段
     *
     * @param sourceClass 给定的类
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClass) {
        return getFields(sourceClass, true, true, true, true);
    }

    /**
     * 给给定对象的给定字段赋值
     *
     * @param object              给定的对象
     * @param fieldName           要赋值的字段名称
     * @param fieldValue          要赋值的值
     * @param isFindDeclaredField 是否查找Declared类型的字段
     * @param isUpwardFind        是否在父类中查找给定字段
     * @return 赋值成功返回true， 赋值失败返回false
     */
    public static boolean setField(Object object, String fieldName, Object fieldValue,
                                   boolean isFindDeclaredField, boolean isUpwardFind) {
        boolean result = false;
        Field field = getField(object.getClass(), fieldName, isFindDeclaredField, isUpwardFind);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(object, fieldValue);
                result = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }


    /**
     * 从指定的类中获取指定的方法
     * @param sourceClass          给定的类
     * @param methodName           要获取的方法的名字
     * @param isFindDeclaredMethod 是否查找Declared类型的方法
     * @param isUpwardFind         是否在父类中查找给定方法
     * @param methodParameterTypes 方法参数类型列表
     * @return 给定的类中给定名称以及给定参数类型的方法，没有找到返回NULL
     */
    public static Method getMethod(Class<?> sourceClass, String methodName, boolean isFindDeclaredMethod,
                                   boolean isUpwardFind, Class<?>... methodParameterTypes) {
        Method method = null;
        try {
            method = isFindDeclaredMethod ? sourceClass.getDeclaredMethod(methodName, methodParameterTypes) :
                    sourceClass.getMethod(methodName, methodParameterTypes);
        } catch (NoSuchMethodException e) {
            if (isUpwardFind) {
                Class<?> parentClass = sourceClass.getSuperclass();
                while (parentClass != null && method == null) {
                    try {
                        method = isFindDeclaredMethod?sourceClass.getDeclaredMethod(methodName, methodParameterTypes):
                                sourceClass.getMethod(methodName, methodParameterTypes);
                    } catch (NoSuchMethodException e1) {
                        parentClass = sourceClass.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 从指定的类中获取指定的方法，默认获取Declared类型的方法、向上查找
     *
     * @param sourceClass          指定的类
     * @param methodName           方法名
     * @param methodParameterTypes 方法参数类型列表
     * @return
     */
    public static Method getMethod(Class<?> sourceClass, String methodName, Class<?>... methodParameterTypes) {
        return getMethod(sourceClass, methodName, true, true, methodParameterTypes);
    }

    /**
     * 从指定的类中获取指定名称的不带任何参数的方法，默认获取Declared类型的方法并且向上查找
     *
     * @param sourceClass 指定的类
     * @param methodName  方法名
     * @return
     */
    public static Method getMethod(Class<?> sourceClass, String methodName) {
        return getMethod(sourceClass, methodName, EMPTY_PARAM_TYPES);
    }

    /**
     * 获取给定类的所有方法
     *
     * @param sourceClass          给定的类
     * @param isGetDeclaredMethod 是否需要获取Declared方法
     * @param isFromSuperClassGet 是否需要获取父类中的方法
     * @param isDESCGet           在最终获取的列表里，父类的方法是否需要排在子类的前面。只有需要把其父类中的方法也取出时此参数才有效
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> sourceClass, boolean isGetDeclaredMethod, boolean isFromSuperClassGet, boolean isDESCGet) {
        List<Method> methodList = new ArrayList<>();
        //如果需要从父类中获取
        if (isFromSuperClassGet) {
            //获取当前类的所有父类
            List<Class<?>> classList = getSuperClasss(sourceClass, true);
            //如果是降序获取
            if (isDESCGet) {
                for (int w = classList.size() - 1; w > -1; w--) {
                    methodList.addAll(Arrays.asList(isGetDeclaredMethod ? classList.get(w).getDeclaredMethods() : classList.get(w).getMethods()));
                }
            } else {
                for (Class<?> aClassList : classList) {
                    methodList.addAll(Arrays.asList(isGetDeclaredMethod ? aClassList.getDeclaredMethods() : aClassList.getMethods()));
                }
            }
        } else {
            methodList.addAll(Arrays.asList(isGetDeclaredMethod ? sourceClass.getDeclaredMethods() : sourceClass.getMethods()));
        }
        return methodList;
    }

    /**
     * 获取给定类的所有方法
     *
     * @param sourceClass 给定的类
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> sourceClass) {
        return getMethods(sourceClass, true, true, true);
    }

    /**
     * 调用不带参数的方法
     *
     * @param method 要调用的类中的给定方法
     * @param object 给定类的对象
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Method method, Object object) throws
            Exception {
        return method.invoke(object, EMPTY_PARAMS);
    }


    /**
     * 获取给定的类中给定参数类型的构造函数
     *
     * @param sourceClass               给定的类
     * @param isFindDeclaredConstructor 是否查找Declared构造函数
     * @param isUpwardFind              是否向上去其父类中寻找
     * @param constructorParameterTypes 构造函数的参数类型
     * @return 给定的类中给定参数类型的构造函数
     */
    public static Constructor<?> getConstructor(Class<?> sourceClass, boolean isFindDeclaredConstructor, boolean isUpwardFind, Class<?>... constructorParameterTypes) {
        Constructor<?> method = null;
        try {
            method = isFindDeclaredConstructor ? sourceClass.getDeclaredConstructor(constructorParameterTypes) : sourceClass.getConstructor(constructorParameterTypes);
        } catch (NoSuchMethodException e1) {
            if (isUpwardFind) {
                Class<?> classs = sourceClass.getSuperclass();
                while (method == null && classs != null) {
                    try {
                        method = isFindDeclaredConstructor ? sourceClass.getDeclaredConstructor(constructorParameterTypes) : sourceClass.getConstructor(constructorParameterTypes);
                    } catch (NoSuchMethodException e11) {
                        classs = classs.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 获取给定的类中所有的构造函数
     *
     * @param sourceClass               给定的类
     * @param isFindDeclaredConstructor 是否需要获取Declared构造函数
     * @param isFromSuperClassGet       是否需要把其父类中的构造函数也取出
     * @param isDESCGet                 在最终获取的列表里，父类的构造函数是否需要排在子类的前面。只有需要把其父类中的构造函数也取出时此参数才有效
     * @return 给定的类中所有的构造函数
     */
    public static List<Constructor<?>> getConstructors(Class<?> sourceClass, boolean isFindDeclaredConstructor, boolean isFromSuperClassGet, boolean isDESCGet) {
        List<Constructor<?>> constructorList = new ArrayList<Constructor<?>>();
        //如果需要从父类中获取
        if (isFromSuperClassGet) {
            //获取当前类的所有父类
            List<Class<?>> classList = getSuperClasss(sourceClass, true);

            //如果是降序获取
            if (isDESCGet) {
                for (int w = classList.size() - 1; w > -1; w--) {
                    constructorList.addAll(Arrays.asList(isFindDeclaredConstructor ? classList.get(w).getDeclaredConstructors() : classList.get(w).getConstructors()));
                }
            } else {
                for (Class<?> aClassList : classList) {
                    constructorList.addAll(Arrays.asList(isFindDeclaredConstructor ? aClassList.getDeclaredConstructors() : aClassList.getConstructors()));
                }
            }
        } else {
            constructorList.addAll(Arrays.asList(isFindDeclaredConstructor ? sourceClass.getDeclaredConstructors() : sourceClass.getConstructors()));
        }
        return constructorList;
    }


    /**
     * 获取给定的类所有的父类
     *
     * @param sourceClass       给定的类
     * @param isAddCurrentClass 是否将当年类放在最终返回的父类列表的首位
     * @return 给定的类所有的父类
     */
    public static List<Class<?>> getSuperClasss(Class<?> sourceClass, boolean isAddCurrentClass) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        Class<?> classs;
        if (isAddCurrentClass) {
            classs = sourceClass;
        } else {
            classs = sourceClass.getSuperclass();
        }
        while (classs != null) {
            classList.add(classs);
            classs = classs.getSuperclass();
        }
        return classList;
    }


    /**
     * 获取给定的类的名字
     *
     * @param sourceClass 给定的类
     * @return 给定的类的名字
     */
    public static String getClassName(Class<?> sourceClass) {
        String classPath = sourceClass.getName();
        return classPath.substring(classPath.lastIndexOf('.') + 1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> clas) {
        if (object != null && fieldName != null && fieldName.length() != 0 && clas != null) {
            try {
                Field field = ReflectUtils.getField(object.getClass(), fieldName, true, true);
                if (field != null) {
                    field.setAccessible(true);
                    return (T) field.get(object);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 判断给定字段是否是type类型的数组
     *
     * @param field
     * @param type
     * @return
     */
    public static boolean isArrayByType(Field field, Class<?> type) {
        Class<?> fieldType = field.getType();
        return fieldType.isArray() && type.isAssignableFrom(fieldType.getComponentType());
    }

    /**
     * 判断给定字段是否是type类型的collectionType集合，例如collectionType=List.class，type=Date.class就是要判断给定字段是否是Date类型的List
     *
     * @param field
     * @param collectionType
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isCollectionByType(Field field, Class<? extends Collection> collectionType, Class<?> type) {
        Class<?> fieldType = field.getType();
        if (collectionType.isAssignableFrom(fieldType)) {
            Class<?>
                    first = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            return type.isAssignableFrom(first);
        } else {
            return false;
        }
    }
}
