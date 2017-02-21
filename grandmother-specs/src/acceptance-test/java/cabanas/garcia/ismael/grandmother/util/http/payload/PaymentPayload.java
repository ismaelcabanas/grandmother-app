package cabanas.garcia.ismael.grandmother.util.http.payload;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by XI317311 on 19/02/2017.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class PaymentPayload {
    private Long paymentTypeId;
    private String amount;
    private String dateOfPayment;
    private String description;
}
