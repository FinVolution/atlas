package com.ppdai.atlas.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by yinzuolong on 2017/11/22.
 */
public class SerializeTest {

    @Test
    public void name() throws Exception {
        CloudDto cloudDto = new CloudDto();
        cloudDto.setName("test");
        cloudDto.setInsertTime(new DateTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cloudDto);
        System.out.println(json);
        CloudDto c = objectMapper.readValue(json, CloudDto.class);
        System.out.println(c);

    }
}
