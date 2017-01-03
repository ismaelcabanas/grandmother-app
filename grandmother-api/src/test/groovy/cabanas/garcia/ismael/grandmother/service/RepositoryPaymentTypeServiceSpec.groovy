package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTypeRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPaymentTypeService
import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeTestUtil.*
import spock.lang.Specification


/**
 * Created by XI317311 on 03/01/2017.
 */
class RepositoryPaymentTypeServiceSpec extends Specification{

    def "findAll() should call to repository for getting payment types order by name"(){
        given:
            PaymentTypeRepository paymentTypeRepository = Mock(PaymentTypeRepository)
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: paymentTypeRepository)
        when:
            paymentTypeService.findAll()
        then:
            1 * paymentTypeRepository.findAllByOrderByNameAsc()
    }

    def "findAll() should return a payment type list ordered by name in ascending order"(){
        given:
            List<PaymentType> paymentTypes = new ArrayList<>()
            paymentTypes.add(getEndesaPayment())
            paymentTypes.add(getGasPayment())
            paymentTypes.add(getAguaPayment())
        and:
            PaymentTypeRepository paymentTypeRepository = Mock(PaymentTypeRepository)
            paymentTypeRepository.findAllByOrderByNameAsc() >> paymentTypes.sort {it.name}
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: paymentTypeRepository)
        when:
            List<PaymentType> paymentTypesResult = paymentTypeService.findAll()
        then:
            paymentTypesResult != null
            paymentTypesResult.size() == 3
            paymentTypesAre(paymentTypesResult, getAguaPayment(), getEndesaPayment(), getGasPayment())
    }

    def void paymentTypesAre(List<PaymentType> result, PaymentType... paymentTypes) {
        paymentTypes.eachWithIndex { PaymentType paymentType, int i ->
            assert result.get(i).name == paymentType.name
        }
    }
}
