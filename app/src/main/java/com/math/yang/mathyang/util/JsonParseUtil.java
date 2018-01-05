package com.math.yang.mathyang.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.math.yang.mathyang.model.BookTerm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtx on 2018/1/4.
 */

public class JsonParseUtil {
    public static ObjectMapper mapper = new ObjectMapper();

    public static List<BookTerm> parseBookList(String s) {
        List<BookTerm> columnList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(s);
            JSONArray dataArray = json.getJSONArray(Constant.DATA);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = getCollectionType(ArrayList.class, BookTerm.class);
            columnList = mapper.readValue(String.valueOf(dataArray), javaType);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return columnList;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
