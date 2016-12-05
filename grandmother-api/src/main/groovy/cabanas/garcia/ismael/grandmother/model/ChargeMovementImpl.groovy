package cabanas.garcia.ismael.grandmother.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class ChargeMovementImpl extends MovementImpl implements ChargeMovement{

    Category category
    ChargeType chargeType

    @Override
    Category getCategory() {
        return category
    }

    @Override
    ChargeType getChargeType() {
        return chargeType
    }
}
