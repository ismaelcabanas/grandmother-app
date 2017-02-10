package cabanas.garcia.ismael.grandmother.utils

import java.util.function.Function
import java.util.stream.Collectors

/**
 * Created by XI317311 on 22/12/2016.
 */
class Converter {
    static <T,E> Collection<E> convert(Collection<T> source, Function<T,E> function) {
        return source.stream()
                .map(function)
                .collect(Collectors.<T> toList())
    }
}
