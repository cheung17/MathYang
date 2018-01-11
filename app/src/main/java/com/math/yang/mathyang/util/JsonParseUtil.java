package com.math.yang.mathyang.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.math.yang.mathyang.model.BookTerm;
import com.math.yang.mathyang.model.UnitVideo;

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

    public static List<UnitVideo> parseUnitList(String s) {
        List<UnitVideo> columnList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(s);
            JSONArray dataArray = json.getJSONArray(Constant.DATA);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = getCollectionType(ArrayList.class, UnitVideo.class);
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

    /**
     * 用新的更新旧的
     * @param newList
     * @param oldList
     */
    public static void updateUnitList(List<UnitVideo> newList, List<UnitVideo> oldList) {
        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < oldList.size(); j++) {
                if (oldList.get(j).getBookid().equals(newList.get(i).getBookid())) {
                     //
                    oldList.get(j).setTitle(newList.get(i).getTitle());
                    oldList.get(j).setTitle(newList.get(i).getTitle());
                    oldList.get(j).setTitle(newList.get(i).getTitle());
                    oldList.get(j).setTitle(newList.get(i).getTitle());
                    oldList.get(j).setTitle(newList.get(i).getTitle());
                }
            }
        }
    }
}
