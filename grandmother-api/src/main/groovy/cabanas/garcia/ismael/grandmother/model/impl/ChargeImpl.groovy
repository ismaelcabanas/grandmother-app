package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Charge
import cabanas.garcia.ismael.grandmother.model.ChargeType
import cabanas.garcia.ismael.grandmother.service.ChargeService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class ChargeImpl implements Charge{
    private ChargeService chargeService
    private BigDecimal amount
    private ChargeType type
    private Date date

    @Override
    def create() {
        chargeService.create(this)
    }

    @Override
    BigDecimal getAmount() {
        return amount
    }

    @Override
    ChargeType getType() {
        return type
    }

    @Override
    Date getDate() {
        return date
    }
}
