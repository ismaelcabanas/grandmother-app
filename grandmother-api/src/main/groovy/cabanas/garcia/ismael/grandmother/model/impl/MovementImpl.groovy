package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Movement
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
abstract class MovementImpl implements Movement{
    BigDecimal amount
    Date dateOfMovement

    @Override
    BigDecimal getAmount() {
        return amount
    }

    @Override
    Date getDateOfMovement() {
        return dateOfMovement
    }
}
