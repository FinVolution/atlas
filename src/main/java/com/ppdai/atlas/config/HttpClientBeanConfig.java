package com.ppdai.atlas.config;

import com.ppdai.atlas.client.JsonHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangyicong on 18-7-25.
 */

@Configuration
public class HttpClientBeanConfig {

    @Value("${alertmanager.connTimeout:1000}")
    private int casConnTimeout;

    @Value("${alertmanager.readTimeout:1000}")
    private int casReadTimeout;

    @Bean(name = "alertManagerHttpClient")
    public JsonHttpClient alertManagerHttpClient() {
        return new JsonHttpClient(casConnTimeout, casReadTimeout);
    }
}
