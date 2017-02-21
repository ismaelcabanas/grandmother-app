package cabanas.garcia.ismael.grandmother.util.http;

import cabanas.garcia.ismael.grandmother.util.JsonUtil;
import cabanas.garcia.ismael.grandmother.util.http.Header;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.apache.http.impl.io.EmptyInputStream;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by XI317311 on 16/02/2017.
 */
public class Response<T> {
    private int statusCode;
    private InputStream content;
    private List<Header> headers;
    private Class<T> targetClass;

    public Response(int statusCode, InputStream content, List<Header> headers, Class<T> targetClass) {
        this.statusCode = statusCode;
        this.content = content;
        this.headers = headers;
        this.targetClass = targetClass;
    }

    public Optional<Header> getHeader(String name) {
        Predicate<Header> headerNameFilter = (header -> header.getName().equalsIgnoreCase(name));

        Optional<Header> firstOcurrence = headers.stream().filter(headerNameFilter).findFirst();

        return firstOcurrence;
    }


    public Optional<T> getContent(){
        T object = JsonUtil.toObject(content, targetClass);

        return (Optional<T>) Optional.ofNullable(object);
    }

    public List<T> getContentList() {
        List<T> listObjects = JsonUtil.toOjectList(content, targetClass);

        return (List<T>) listObjects;
    }
}
