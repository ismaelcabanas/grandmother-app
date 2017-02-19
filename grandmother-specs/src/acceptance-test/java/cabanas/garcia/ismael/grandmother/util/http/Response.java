package cabanas.garcia.ismael.grandmother.util.http;

import cabanas.garcia.ismael.grandmother.util.http.Header;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by XI317311 on 16/02/2017.
 */
@Builder
@Value
public class Response<T> {
    private int statusCode;
    private T content;
    private List<Header> headers;

    public Optional<Header> getHeader(String name) {
        Predicate<Header> headerNameFilter = (header -> header.getName().equalsIgnoreCase(name));

        Optional<Header> firstOcurrence = headers.stream().filter(headerNameFilter).findFirst();

        return firstOcurrence;
    }
}
