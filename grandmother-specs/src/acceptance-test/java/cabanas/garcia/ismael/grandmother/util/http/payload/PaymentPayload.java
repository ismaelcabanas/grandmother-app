package cabanas.garcia.ismael.grandmother.util.http.payload;

import lombok.*;

import java.math.BigDecimal;

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
