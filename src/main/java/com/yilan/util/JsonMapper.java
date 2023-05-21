package com.yilan.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @description: 简单封装Jackson，实现JSON String<->Java Object的Mapper
 * @date: Created in 2022/10/14 09:56
 */
public class JsonMapper {

    /**LOGGER**/
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);

    /**objectMapper对象 fastjson**/
    private static ObjectMapper objectMapper;

    /**
     * JsonMapper 构造函数
     * @param inclusion inclusion
     * */
    public JsonMapper(JsonInclude.Include inclusion) {
        objectMapper = new ObjectMapper();
        //设置输出时包含属性的风格
        objectMapper.setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()来反序列化Enum,非常危险
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false) ;
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     * @return JsonMapper
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(JsonInclude.Include.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     * @return JsonMapper
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     * @return JsonMapper
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
     * @return JsonMapper
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 输出全部属性
     * @param object object
     * @return String
     */
    public static String toNormalJson(Object object){
        return new JsonMapper(JsonInclude.Include.ALWAYS).toJson(object);
    }

    /**
     * 输出非空属性
     * @param object object
     * @return String
     */
    public static String toNonNullJson(Object object){
        return new JsonMapper(JsonInclude.Include.NON_NULL).toJson(object);
    }

    /**
     * 输出初始值被改变部分的属性
     * @param object object
     * @return String
     */
    public static String toNonDefaultJson(Object object){
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT).toJson(object);
    }

    /**
     * 输出非Null且非Empty(如List.isEmpty)的属性
     * @param object object
     * @return String
     */
    public static String toNonEmptyJson(Object object){
        return new JsonMapper(JsonInclude.Include.NON_EMPTY).toJson(object);
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     * @param object object
     * @return String
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     * @param object object
     * @param pretty pretty
     * @return String
     */
    public String toJson(Object object, boolean pretty) {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return toJson(object);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函数constructParametricType构造类型.
     * @see #constructParametricType(Class, Class...)
     * @param jsonString jsonString
     * @param clazz clazz
     * @param <T> T
     * @return T
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函数constructParametricType构造类型.
     * @see #constructParametricType(Class, Class...)
     * @param jsonString jsonString
     * @param javaType javaType
     * @param <T> T
     * @return T
     */
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * fromJson
     * @param jsonString jsonString
     * @param parametrized parametrized
     * @param parameterClasses parameterClasses
     * @param <T> T
     * @return T
     * */
    public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        return this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
    }

    /**
     * fromJsonToList
     * @param jsonString jsonString
     * @param classMeta classMeta
     * @param <T> T
     * @return List<T>
     * */
    public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta){
        return this.fromJson(jsonString, constructParametricType(List.class, classMeta));
    }

    /**
     * fromJson
     * @param node node
     * @param parametrized parametrized
     * @param parameterClasses parameterClasses
     * @param <T> T
     * @return T
     * */
    public <T> T fromJson(JsonNode node, Class<?> parametrized, Class<?>... parameterClasses) {
        JavaType javaType = constructParametricType(parametrized, parameterClasses);
        try {
            JsonParser parser = objectMapper.treeAsTokens(node);
            return objectMapper.readValue(parser, javaType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * pathAtRoot
     * @param json json
     * @param path path
     * @param parametrized parametrized
     * @param parameterClasses parameterClasses
     * @param <T> T
     * @return T
     * */
    public <T> T pathAtRoot(String json, String path, Class<?> parametrized, Class<?>... parameterClasses){
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return fromJson(node, parametrized, parameterClasses);
    }

    /**
     * pathAtRoot
     * @param json json
     * @param path path
     * @param clazz clazz
     * @param <T> T
     * @return T
     * */
    @SuppressWarnings("unchecked")
    public <T> T pathAtRoot(String json, String path, Class<T> clazz){
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return fromJson(node, clazz);
    }

    /**
     * 构造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
     *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
     * @param parametrized parametrized
     * @param parameterClasses parameterClasses
     * @return JavaType
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 解析json的节点
     * @param json json
     * @return JsonNode
     * */
    public JsonNode parseNode(String json){
        try {
            return objectMapper.readValue(json, JsonNode.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

