package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPaymentTypeService
import cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static PaymentTypeUtil.*

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
@Transactional // con esta anotación indicamos que cada test se ejecute en una transacción e inmediatamente después de la ejecución del test se hará un rollback
//@DirtiesContext // What it does is mark the ApplicationContext as dirty, thus requiring it to be reloaded for the next integration test
class PaymentTypeServiceCRUDITSpec extends Specification{
    public static final String WATER_CHARGE = "Agua"
    @Autowired
    PaymentTypeRepository chargeTypeRepository

    def "remove all charge types"(){
        given: "payment type service"
            PaymentTypeService chargeTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: chargeTypeRepository)
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
            PaymentTypeService chargeTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: chargeTypeRepository)
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

    def "read all payment types ordered ascending by name"(){
        given: "payment type service"
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: chargeTypeRepository)
        and: "tipos de pago dados de alta en el sistema"
            paymentTypeService.create(getGasPayment())
            paymentTypeService.create(getEndesaPayment())
            paymentTypeService.create(getAguaPayment())
        when:
            List<PaymentType> paymentTypes = paymentTypeService.findAll()
        then:
            paymentTypes != null
            paymentTypes.size() == 3
            paymentTypesAre(paymentTypes, getAguaPayment(), getEndesaPayment(), getGasPayment())
    }

    def void paymentTypesAre(List<PaymentType> result, PaymentType... paymentTypes) {
        paymentTypes.eachWithIndex { PaymentType paymentType, int i ->
            assert result.get(i).name == paymentType.name
            assert result.get(i).id != null
        }
    }
}
