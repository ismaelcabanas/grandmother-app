package cabanas.garcia.ismael.grandmother.model;

import lombok.*;

@Builder
@EqualsAndHashCode(exclude = "id")
@Value
@AllArgsConstructor
@Getter
public class PaymentType {
    private Long id;
    private String name;
}
