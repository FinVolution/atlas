package com.ppdai.atlas.entity.converter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;


@Slf4j
@Converter(autoApply = true)
public class MapToStringConverter implements AttributeConverter<Map<String, Object>, String> {


    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        try {
            return JSON.toJSONString(map);
        } catch (Exception e) {
            log.info("transforming map to JSON string error!");
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {

        try {
            return JSON.parseObject(dbData, Map.class);
        } catch (Exception e) {
            log.info("transforming JSON string to Map error!");
            log.info(e.getMessage());
            return Maps.newHashMap();
        }

    }
}
