package cabanas.garcia.ismael.grandmother.domain

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.Movement

/**
 * Created by XI317311 on 05/12/2016.
 */
interface ChargeMovement {
    ChargeType getChargeType()
}