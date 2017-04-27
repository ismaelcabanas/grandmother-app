package cabanas.garcia.ismael.grandmother.controller.response

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

@ToString
@Builder
@EqualsAndHashCode
class AccountBalanceResponse {
    BigDecimal balance
}
