package cabanas.garcia.ismael.grandmother.controller.request

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.validation.constraints.NotNull

/**
 * Created by XI317311 on 13/12/2016.
 */
@ToString
@EqualsAndHashCode
class ChargeRequestBody {

    @NotNull
    String chargeTypeId

    @NotNull
    BigDecimal charge

    @NotNull
    Date dateOfCharge
}
