package com.ppdai.atlas.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class TestHelper {

    //return object from JSON string
    public static <T> T getTestObject(String rs, Class<T> clazz) throws Exception {
        return JSON.parseObject(readSourceFile(rs), clazz);
    }

    //return object list from JSON array string
    public static <T> List<T> getTestList(String rs, Class<T> clazz) throws Exception {
        return JSON.parseArray(readSourceFile(rs), clazz);
    }

    //return JSON string from json file
    public static String readSourceFile(String rs) throws Exception {
        Class<TestHelper> testHelperClass = TestHelper.class;
        ClassLoader classLoader = testHelperClass.getClassLoader();
        URI uri = classLoader.getResource(rs).toURI();
        byte[] bytes = Files.readAllBytes(Paths.get(uri));
        return new String(bytes, "UTF-8");
    }
}
