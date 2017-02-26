package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by XI317311 on 16/02/2017.
 */
public final class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //configure Object mapper for pretty print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    private JsonUtil() {
    }


    public static <T> String toJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialize process failed.", e);
        }
    }

    public static <T> T toObject(InputStream inputStream, Class<T> targetClass) {
        try {
            return objectMapper.readValue(inputStream, targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Serialize process failed.", e);
        }
    }

    public static <T> List<T> toOjectList(InputStream inputStream, Class<T> targetClass) {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
        try {
            return objectMapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new RuntimeException("Serialize process failed.", e);
        }
    }
}
