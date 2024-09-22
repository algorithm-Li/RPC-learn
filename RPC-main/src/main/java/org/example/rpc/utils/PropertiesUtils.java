package org.example.rpc.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.annotation.PropertiesField;
import org.example.rpc.annotation.PropertiesPrefix;
import org.example.rpc.spi.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;


import java.lang.reflect.Field;

/**
 * @description:
 */
public class PropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 根据对象中的配置匹配配置文件
     * @param o
     * @param environment
     */
    public static void init(Object o, Environment environment){
        final Class<?> aClass = o.getClass();
        // 获取前缀
        final PropertiesPrefix prefixAnnotation = aClass.getAnnotation(PropertiesPrefix.class);
        if (prefixAnnotation == null){
            throw new NullPointerException(aClass + " @PropertiesPrefix 不存在");
        }
        String prefix = prefixAnnotation.value();
        // 前缀参数矫正
        if (!prefix.contains(".")){
            prefix += ".";
        }
        // 遍历对象中的字段
        for (Field field : aClass.getDeclaredFields()) {
            //获取字段中有@PropertiesField注解的字段
            final PropertiesField fieldAnnotation = field.getAnnotation(PropertiesField.class);
            //如果没有，遍历下一个
            if (fieldAnnotation == null)
                continue;
            //有，拿出注解的值，如果为空或者为null，则是按字段名进行修改
            String fieldValue = fieldAnnotation.value();
            if(fieldValue == null || fieldValue.equals("")){
                fieldValue = convertToHyphenCase(field.getName());
            }
            try {
                // 赋值
                field.setAccessible(true);
                final Class<?> type = field.getType();
                final Object value = PropertyUtil.handle(environment, prefix + fieldValue, type);
                if(value == null) continue;
                //打印日志
                logger.info("{}{}value=={}", prefix, fieldValue, value.toString());
                field.set(o, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
    }


    /**
     * //将驼峰的字典名改成用 - 连接， 例如: registryType 变成 registry-type
     * @param input  字段名
     */
    public static String convertToHyphenCase(String input) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                output.append('-');
                output.append(Character.toLowerCase(c));
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }
}
