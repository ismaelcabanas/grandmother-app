package cabanas.garcia.ismael.grandmother.controller.request

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.validation.constraints.NotNull

/**
 * Created by XI317311 on 13/12/2016.
 */
@ToString
@EqualsAndHashCode
@Builder
class DepositRequestBody {

    @NotNull
    Long personId

    @NotNull
    BigDecimal deposit

    @NotNull
    Date dateOfDeposit

    @NotNull
    String description
}
