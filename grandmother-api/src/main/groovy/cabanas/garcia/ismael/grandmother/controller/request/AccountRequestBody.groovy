package cabanas.garcia.ismael.grandmother.controller.request

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

/**
 * Created by XI317311 on 15/12/2016.
 */
@ToString
@EqualsAndHashCode
@Builder
class AccountRequestBody {

    @NotEmpty
    String accountNumber

    BigDecimal balance
}
