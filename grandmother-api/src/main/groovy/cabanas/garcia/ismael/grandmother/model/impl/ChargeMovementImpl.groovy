package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.ChargeMovement
import cabanas.garcia.ismael.grandmother.model.ChargeType
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

    ChargeType chargeType

    @Override
    ChargeType getChargeType() {
        return chargeType
    }
}
