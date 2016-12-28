package cabanas.garcia.ismael.grandmother.controller.response

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 13/12/2016.
 */
@ToString
@Builder
@EqualsAndHashCode
class AccountResponse {
    BigDecimal balance
    String accountNumber
    Long id
}
