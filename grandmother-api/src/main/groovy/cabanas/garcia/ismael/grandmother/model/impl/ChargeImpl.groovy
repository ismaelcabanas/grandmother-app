package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Charge
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
    private String name
    private ChargeService chargeService

    @Override
    def create() {
        chargeService.create(this)
    }
}
