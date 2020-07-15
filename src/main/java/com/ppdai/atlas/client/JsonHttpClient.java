package com.ppdai.atlas.client;

import com.alibaba.fastjson.JSON;
import com.ppdai.atlas.utils.JsonUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JsonHttpClient {

    private static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    private void throwIfFailedResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("code=" + response.code() + ", status=" + response.message());
        }
    }

    public JsonHttpClient(int connTimeout, int readTimeout) {
        client = new OkHttpClient.Builder().connectTimeout(connTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
    }

    public JsonHttpClient() {
        this(10000, 10000);
    }

    public String post(String url, Object reqObj) throws IOException {
        String json = "";
        if (reqObj != null) {
            json = JSON.toJSONString(reqObj);
        }
        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            throwIfFailedResponse(response);

            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String post(String url, Map<String, String> headers, Object reqObj) throws IOException {
        String json = "";
        if (reqObj != null) {
            json = JsonUtil.toJson(reqObj);
        }
        RequestBody body = RequestBody.create(JSONTYPE, json);

        Headers.Builder hb = new Headers.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            hb.add(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder().url(url).headers(hb.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            throwIfFailedResponse(response);

            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String delete(String url, Map<String, String> headers) throws IOException {
        Headers.Builder hb = new Headers.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            hb.add(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder().url(url).headers(hb.build()).delete().build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            throwIfFailedResponse(response);

            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public <T> T post(String url, Object request, Class<T> tClass) throws IOException {
        String rs = post(url, request);
        if (rs == null || rs.length() == 0 || rs.trim().length() == 0) {
            return null;
        } else {
            return JsonUtil.parseJson(rs, tClass);
        }
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            throwIfFailedResponse(response);

            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public <T> T get(String url, Class<T> tClass) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            throwIfFailedResponse(response);

            return JsonUtil.parseJson(response.body().string(), tClass);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
