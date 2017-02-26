package cabanas.garcia.ismael.grandmother.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by XI317311 on 15/02/2017.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Payment {
    private String paymentType;
    private String amount;
    private String date;
}
