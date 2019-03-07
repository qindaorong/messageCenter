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
    public String httpGet(String url,Map<String,String> headMap) throws BusinessException {
        try {
            return this.httpGetResponse(url,headMap).body().string();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpPost(String url, String json,Map<String,String> headMap) throws BusinessException {
        try {
            return this.httpPostResponse(url,json,headMap).body().string();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPostRes(String url, String json,Map<String,String> headMap) throws BusinessException {
        return this.httpPostResponse(url,json,headMap);
    }

    /**
     * put
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpPut(String url, String json,Map<String,String> headMap) throws BusinessException {
        try {
            return this.httpPutResponse(url,json,headMap).body().string();
        } catch (IOException e) {
           throw new BusinessException(e);
        }
    }

    /**
     * put 修改请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPutRes(String url, String json,Map<String,String> headMap) throws BusinessException {
        return this.httpPutResponse(url,json,headMap);
    }

    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String httpDelete(String url,String json,Map<String,String> headMap)throws BusinessException {
        try {
            return this.httpDeleteResponse(url,json,headMap).body().string();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }


    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpDeleteRes(String url,String json,Map<String,String> headMap)throws BusinessException {
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
    public Response httpGetResponse(String url,Map<String,String> headMap) throws BusinessException {
        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .build();
        Response response ;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return response;
    }


    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPostResponse(String url, String json,Map<String,String> headMap) throws BusinessException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        Response response ;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return response;
    }


    /**
     * put
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpPutResponse(String url, String json,Map<String,String> headMap) throws BusinessException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .put(requestBody)
                .build();
        Response response ;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return response;
    }

    /**
     * delete
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpDeleteResponse(String url,String json,Map<String,String> headMap)throws BusinessException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }

        Request request = builder
                .url(url)
                .delete(requestBody)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return response;
    }



    /**
     * post
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public Response httpFormPostResponse(String url, String json,Map<String,String> headMap,Map<String,String> formMap) throws BusinessException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isEmpty(headMap)){
            this.addHeaderMap(builder,headMap);
        }
        Request request = builder
                .url(url)
                .post(this.addFormBody(formMap))
                .build();
        Response response ;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return response;
    }

    private FormBody addFormBody(Map<String,String> formMap){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : formMap.keySet()) {
            //追加表单信息
            builder.add(key, formMap.get(key));
        }
        return builder.build();
    }

}
