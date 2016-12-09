package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.ChargeTypeServiceImpl
import cabanas.garcia.ismael.grandmother.service.impl.PersonServiceImpl
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
        given: "charge type service"
            ChargeTypeService chargeTypeService = new ChargeTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
        when: "delete all charge types"
            chargeTypeService.deleteAll()
        then:
            List<ChargeType> allChargeTypes = chargeTypeService.findAll()
            allChargeTypes.isEmpty()
    }

    def "create charge types"(){
        given: "a charge type data"
            ChargeType chargeType = ChargeType.builder().name(WATER_CHARGE).build()
        and: "charge type service"
            ChargeTypeService chargeTypeService = new ChargeTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
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
