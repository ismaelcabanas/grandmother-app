package cabanas.garcia.ismael.grandmother.util.http;

import lombok.Builder;
import lombok.Value;

/**
 * Created by XI317311 on 16/02/2017.
 */
@Builder
@Value
public class Header {
    private String name;
    private String value;
}
