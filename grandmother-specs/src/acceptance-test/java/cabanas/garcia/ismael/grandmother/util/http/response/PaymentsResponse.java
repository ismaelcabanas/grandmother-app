package cabanas.garcia.ismael.grandmother.util.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created by XI317311 on 20/02/2017.
 */
@Builder
@AllArgsConstructor
@Getter
public class PaymentsResponse {
    private String total;
    private PaymentResponse[] payments;
}
