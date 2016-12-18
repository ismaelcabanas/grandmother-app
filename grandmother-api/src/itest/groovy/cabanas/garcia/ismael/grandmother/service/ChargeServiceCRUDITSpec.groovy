package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.PaymentTypeServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
@Stepwise
class ChargeServiceCRUDITSpec extends Specification{
    public static final String WATER_CHARGE = "Agua"
    @Autowired
    ChargeTypeRepository chargeTypeRepository

    def "remove all charge types"(){
        given: "payment type service"
            PaymentTypeService chargeTypeService = new PaymentTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
        when: "delete all payment types"
            chargeTypeService.deleteAll()
        then:
            List<PaymentType> allChargeTypes = chargeTypeService.findAll()
            allChargeTypes.isEmpty()
    }

    def "create charge types"(){
        given: "a payment type data"
            PaymentType chargeType = PaymentType.builder().name(WATER_CHARGE).build()
        and: "payment type service"
            PaymentTypeService chargeTypeService = new PaymentTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
        when:
            chargeTypeService.create(chargeType)
        then:
            List<Person> allChargeTypes = chargeTypeService.findAll()
            allChargeTypes != null
        and:
            allChargeTypes.size() == 1
        and:
            with(allChargeTypes.get(0)){
                name == WATER_CHARGE
                id != null
            }
    }

}
