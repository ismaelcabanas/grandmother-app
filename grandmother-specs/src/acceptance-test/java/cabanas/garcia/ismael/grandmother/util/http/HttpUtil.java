package cabanas.garcia.ismael.grandmother.util.http;

import cabanas.garcia.ismael.grandmother.util.JsonUtil;
import cabanas.garcia.ismael.grandmother.util.RegExpUtil;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XI317311 on 16/02/2017.
 */
public class HttpUtil<T> {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final Header CONTENT_TYPE_APPLICATION_JSON_HEADER = new BasicHeader(CONTENT_TYPE, APPLICATION_JSON);

    private String endpoint;
    private final CloseableHttpClient httpClient;
    private final Class<T> targetClass;

    public HttpUtil(String endpoint, Class<T> targetClass) {
        this.endpoint = endpoint;
        this.targetClass = targetClass;
        httpClient = HttpClients.createDefault();

    }

    public <K> Response<T> post(K payload) {
        HttpPost postRequest = new HttpPost(endpoint);

        addContentTypeApplicationJsonHeader(postRequest);

        setPayload(payload, postRequest);

        final HttpResponse httpResponse = execute(postRequest);

        Response response = extractResponse(httpResponse);

        return response;
    }

    public <K> void put(K payload) {
        HttpPut putRequest = new HttpPut(endpoint);

        addContentTypeApplicationJsonHeader(putRequest);

        setPayload(payload, putRequest);

        execute(putRequest);
    }

    public Response<T> get() {
        HttpGet request = new HttpGet(endpoint);

        final HttpResponse httpResponse = execute(request);

        Response<T> response = extractResponse(httpResponse);

        return response;
    }

    public void addPathVariable(String pathVariableName, String pathVariableValue) {
        this.endpoint = RegExpUtil.replacePathVariable(endpoint, pathVariableName, pathVariableValue);
    }

    public void addQueryString(String name, String value) {
        StringBuffer result = new StringBuffer(this.endpoint);
        if(!endpoint.contains("?")){
            result.append("?");
        }

        result.append(name).append("=").append(value).append("&");

        this.endpoint = result.toString();
    }

    private Response<T> extractResponse(HttpResponse httpResponse) {
        Response<T> response = null;
        try {
            response = new Response<T>(httpResponse.getStatusLine().getStatusCode(), httpResponse.getEntity().getContent(),
                    toResponseHeaders(httpResponse.getAllHeaders()), targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Error extracting response from httpResponse.", e);
        }

        return response;
    }

    private <K> void setPayload(K payload, HttpEntityEnclosingRequestBase httpEntityRequest) {
        final String jsonString = JsonUtil.toJson(payload);
        final StringEntity entity;
        try {
            entity = new StringEntity(jsonString);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error processing entity " + jsonString, e);
        }
        httpEntityRequest.setEntity(entity);
    }

    private HttpResponse execute(HttpUriRequest request) {
        final HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Error sending request.", e);
        }
        return httpResponse;
    }

    private List<cabanas.garcia.ismael.grandmother.util.http.Header> toResponseHeaders(Header[] headers) {
        List<cabanas.garcia.ismael.grandmother.util.http.Header> responseHeaders =
                new ArrayList<>();

        Arrays.stream(headers).forEach(header -> responseHeaders.add(
                cabanas.garcia.ismael.grandmother.util.http.Header.builder()
                        .name(header.getName())
                        .value(header.getValue())
                        .build()));
        return responseHeaders;
    }

    private void addContentTypeApplicationJsonHeader(HttpRequest request) {
        request.addHeader(CONTENT_TYPE_APPLICATION_JSON_HEADER);
    }

}
