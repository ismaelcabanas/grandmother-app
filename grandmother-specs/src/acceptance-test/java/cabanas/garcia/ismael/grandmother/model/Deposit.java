package cabanas.garcia.ismael.grandmother.model;

import lombok.*;

@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Deposit {
    private String person;
    private String amount;
    private String date;
}
