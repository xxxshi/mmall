package com.mmall.util;

import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: mmall
 * @description: 序列化:User to Json
 * @author: xxxshi
 * @create: 2018-12-02 09:37
 * @Version: 1.0
 **/
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static{
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象序列化成Json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("object to String warn:",e);
        }
        return null;
    }

    /**
     * 对象序列化成排列好格式Json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToStringPretty(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            log.warn("object to String warn:",e);
        }
        return null;
    }

    /**
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToObject(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }

        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    /**
     * JsonString转为对象集合
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T stringToObject(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }


    public static <T> T stringToObject(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static void main(String[] args) {
        User user1 = new User();
        user1.setUsername("aaa");
        User user2 = new User();
        user2.setUsername("bbb");
        String string1 = JsonUtil.objectToString(user1);
        String string2 = JsonUtil.objectToStringPretty(user1);
        System.out.println(string1);
        System.out.println(string2);
        User user = JsonUtil.stringToObject(string1,User.class);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        String string3 = JsonUtil.objectToStringPretty(userList);
        log.info(string3);
        JsonUtil.stringToObject(string3, new TypeReference <List < User >> (){});
        JsonUtil.stringToObject(string3, List.class, User.class);

    }

}
