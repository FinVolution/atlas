package com.ppdai.atlas.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class JsonUtil {

	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"));
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	public static String toJson(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("tojson 异常", e);
			throw new RuntimeException(e);
		}
	}

	public static <Result> Result parseJson(String json, Class<Result> t) {
		if (isEmpty(json)) {
			return null;
		}
		try {
			return objectMapper.readValue(json, t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("tojson 异常", e);
			throw new RuntimeException(e);
		}
	}

	private static boolean isEmpty(String value) {
		return value == null || value.length() == 0 || value.trim().length() == 0;
	}
}
