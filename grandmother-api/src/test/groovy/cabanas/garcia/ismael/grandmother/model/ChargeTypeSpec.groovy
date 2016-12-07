package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.ChargeImpl
import cabanas.garcia.ismael.grandmother.service.ChargeService
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class ChargeTypeSpec extends Specification{

    def "should create new charges"(){
        given:
        ChargeService mockChargeTypeService = Mock(ChargeService)
        Charge chargeType = new ChargeImpl(name: "Agua", chargeService: mockChargeTypeService)

        when:
        chargeType.create()

        then:
        1 * mockChargeTypeService.create(chargeType)
    }
}
