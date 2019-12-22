package com.illegalaccess.thread.sdk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by xiao on 2019/12/22.
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper om = new ObjectMapper();


    public static String object2String(Object obj) {
        return safeParse(() -> obj);
    }

    public static <T> Optional<T> string2Object(String json, Class<T> clz) {
        try {
            return Optional.ofNullable(om.readValue(json, clz));
        } catch (JsonProcessingException e) {
            log.error("convert json:{} to object:{} fail", json, clz, e);
        }
        return Optional.empty();
    }

    public static <T> Optional<List<T>> string2List(String json, Class<T> clz) {
        // todo

        TypeFactory typeFactory = om.getTypeFactory();
        try {
            return om.readValue(json, typeFactory.constructCollectionType(List.class, clz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static String safeParse(Supplier<Object> realObj) {
        try {
            return om.writeValueAsString(realObj.get());
        } catch (Exception e) {
            return SdkConstants.EMPTY_JSON;
        }
    }
}
