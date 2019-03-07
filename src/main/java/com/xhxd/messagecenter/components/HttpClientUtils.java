package com.xhxd.messagecenter.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xhxd.messagecenter.common.exception.BusinessException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class HttpClientUtils {

    @Autowired
    private OkHttpClient okHttpClient;

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    /**
     * get
     * @param url
     * @return
     * @throws IOException
     */
    public String httpGet(String url,Map<String,String> headMap) throws IOException {
        return this.httpGetResponse(url,headMap).body().string();
    }

    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpPost(String url, String json,Map<String,String> headMap) throws IOException {
        return this.httpPostResponse(url,json,headMap).body().string();
    }

    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPostRes(String url, String json,Map<String,String> headMap) throws IOException {
        return this.httpPostResponse(url,json,headMap);
    }

    /**
     * put
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpPut(String url, String json,Map<String,String> headMap) throws IOException {
        return this.httpPutResponse(url,json,headMap).body().string();
    }

    /**
     * put 修改请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPutRes(String url, String json,Map<String,String> headMap) throws IOException {
        return this.httpPutResponse(url,json,headMap);
    }

    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpDelete(String url,String json,Map<String,String> headMap)throws IOException {
        return this.httpDeleteResponse(url,json,headMap).body().string();
    }


    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpDeleteRes(String url,String json,Map<String,String> headMap)throws IOException {
        return this.httpDeleteResponse(url,json,headMap);
    }



    private void addHeaderMap(Request.Builder builder ,Map<String,String> headMap){
        for (Map.Entry<String, String> entry : headMap.entrySet()) {
            builder.addHeader(entry.getKey(),entry.getValue());
        }
    }


    /**
     * get
     * @param url
     * @return
     * @throws IOException
     */
    public Response httpGetResponse(String url,Map<String,String> headMap) throws IOException {
        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPostResponse(String url, String json,Map<String,String> headMap) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * put
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPutResponse(String url, String json,Map<String,String> headMap) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .put(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpDeleteResponse(String url,String json,Map<String,String> headMap)throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .delete(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * get
     * @param url
     * @return
     * @throws IOException
     */
    public JSONObject httpGetResponse(String url, Map<String,String> headMap, Function<Response, JSONObject> function){
        Response response = null;
        try{
            Request.Builder builder = new Request.Builder();
            if(!CollectionUtils.isEmpty(headMap)){
                this.addHeaderMap(builder,headMap);
            }

            Request request = builder
                    .url(url)
                    .build();
            response = okHttpClient.newCall(request).execute();
            return function.apply(response);
        }catch(IOException e){
            throw new BusinessException(e);
        }finally {
            if(Objects.isNull(response)){
                response.close();
            }
        }
    }


    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public JSONObject httpPostResponse(String url, String json, Map<String,String> headMap, Function<Response, JSONObject> function){
        Response response = null;
        try {
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request.Builder builder = new Request.Builder();
            if (!CollectionUtils.isEmpty(headMap)) {
                this.addHeaderMap(builder, headMap);
            }

            Request request = builder
                    .url(url)
                    .post(requestBody)
                    .build();
            response = okHttpClient.newCall(request).execute();
            return function.apply(response);
        }catch(IOException e){
            throw new BusinessException(e);
        }finally {
            if(Objects.isNull(response)){
                response.close();
            }
        }
    }

    /**
     * post
     * @param url
     * @param json
     * @param headMap
     * @param function
     * @return
     */
    public JSONArray httpPostResponseArray(String url, String json, Map<String,String> headMap, Function<Response, JSONArray> function){
        Response response = null;
        try {
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request.Builder builder = new Request.Builder();
            if (!CollectionUtils.isEmpty(headMap)) {
                this.addHeaderMap(builder, headMap);
            }

            Request request = builder
                    .url(url)
                    .post(requestBody)
                    .build();
            response = okHttpClient.newCall(request).execute();
            return function.apply(response);
        }catch(IOException e){
            throw new BusinessException(e);
        }finally {
            if(Objects.isNull(response)){
                response.close();
            }
        }
    }


    /**
     * put
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public JSONObject httpPutResponse(String url, String json, Map<String,String> headMap, Function<Response, JSONObject> function) {
        Response response = null;
        try {
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request.Builder builder = new Request.Builder();
            if (!CollectionUtils.isEmpty(headMap)) {
                this.addHeaderMap(builder, headMap);
            }

            Request request = builder
                    .url(url)
                    .put(requestBody)
                    .build();
            response = okHttpClient.newCall(request).execute();
            return function.apply(response);
        }catch(IOException e){
            throw new BusinessException(e);
        }finally {
            if(Objects.isNull(response)){
                response.close();
            }
        }
    }

    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public JSONObject httpDeleteResponse(String url, String json, Map<String,String> headMap, Function<Response, JSONObject> function){
        Response response = null;
        try {
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request.Builder builder = new Request.Builder();
            if (!CollectionUtils.isEmpty(headMap)) {
                this.addHeaderMap(builder, headMap);
            }

            Request request = builder
                    .url(url)
                    .delete(requestBody)
                    .build();
            response = okHttpClient.newCall(request).execute();
            return function.apply(response);
        }catch(IOException e){
            throw new BusinessException(e);
        }finally {
            if(Objects.isNull(response)){
                response.close();
            }
        }
    }
}