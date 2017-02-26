package cabanas.garcia.ismael.grandmother.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by XI317311 on 15/02/2017.
 */
@Builder
@Value
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@Getter
@SuppressWarnings("PMD")
public class Account {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
}
