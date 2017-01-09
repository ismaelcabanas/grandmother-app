package cabanas.garcia.ismael.grandmother.controller.response

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 22/12/2016.
 */
@ToString
@EqualsAndHashCode
@Builder
class DepositsResponse {
    BigDecimal total
    Collection<DepositResponse> deposits = new ArrayList<>()
}
