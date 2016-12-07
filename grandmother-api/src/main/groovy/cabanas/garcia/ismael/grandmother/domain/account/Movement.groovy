package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
abstract class Movement {
    protected BigDecimal amount
    protected Date dateOfMovement

    BigDecimal getAmount(){
        amount
    }
    Date getDateOfMovement(){
        dateOfMovement
    }
}