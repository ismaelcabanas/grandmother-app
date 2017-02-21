package cabanas.garcia.ismael.grandmother.util.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by XI317311 on 20/02/2017.
 */
@Builder
@AllArgsConstructor
@Getter
@ToString
public class PaymentTypeResponse {
    private String id;
    private String name;
}
