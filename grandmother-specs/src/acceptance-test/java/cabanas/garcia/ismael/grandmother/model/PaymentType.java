package cabanas.garcia.ismael.grandmother.model;

import lombok.*;

/**
 * Created by XI317311 on 16/02/2017.
 */
@Builder
@EqualsAndHashCode(exclude = "id")
@Value
@AllArgsConstructor
@Getter
public class PaymentType {
    private Long id;
    private String name;
}
