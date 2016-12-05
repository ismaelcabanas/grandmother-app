package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.ChargeTypeImpl
import cabanas.garcia.ismael.grandmother.service.ChargeTypeService
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class ChargeTypeSpec extends Specification{

    def "should create new charges"(){
        given:
        ChargeTypeService mockChargeTypeService = Mock(ChargeTypeService)
        ChargeType chargeType = new ChargeTypeImpl(name: "Agua", chargeTypeService: mockChargeTypeService)

        when:
        chargeType.create()

        then:
        1 * mockChargeTypeService.create(chargeType)
    }
}
