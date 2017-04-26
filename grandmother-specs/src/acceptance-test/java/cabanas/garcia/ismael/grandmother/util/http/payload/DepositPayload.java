package cabanas.garcia.ismael.grandmother.util.http.payload;

import lombok.*;

@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class DepositPayload {
    private Long personId;
    private String deposit;
    private String dateOfDeposit;
    private String description;
}
