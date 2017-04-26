package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Person;
import cabanas.garcia.ismael.grandmother.util.http.Header;
import cabanas.garcia.ismael.grandmother.util.http.HttpUtil;
import cabanas.garcia.ismael.grandmother.util.http.Response;

import javax.swing.*;
import java.util.Optional;

public final class RestUtil {

    public static <T> Response<T> get(String endpoint, Class<T> targetClass) {
        HttpUtil httpUtil = new HttpUtil(endpoint, targetClass);
        return httpUtil.get();
    }

    public static <T, K> Response<T> post(String endpoint, K payload, Class<T> targetClass) {
        HttpUtil httpUtil = new HttpUtil(endpoint, targetClass);
        return httpUtil.post(payload);
    }

    public static <T,K> Response<T> postAndGet(String endpoint, K payload, Class<T> targetClass) {
        Response postResponse = post(endpoint, payload, targetClass);

        final Response[] responseForGetRequest = new Response[1];
        Optional<Header> locationHeader = postResponse.getHeader("Location");
        locationHeader.ifPresent(header -> {
            responseForGetRequest[0] = get(header.getValue(), targetClass);
        });

        Response responseForGet = responseForGetRequest[0];

        return responseForGet;
    }
}
