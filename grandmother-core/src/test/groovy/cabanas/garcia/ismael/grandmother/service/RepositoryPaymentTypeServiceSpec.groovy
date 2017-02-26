package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTypeRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPaymentTypeService
import cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.*

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
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getEndesaPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getGasPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getAguaPersistedPayment())
        and:
            PaymentTypeRepository paymentTypeRepository = Mock(PaymentTypeRepository)
            paymentTypeRepository.findAllByOrderByNameAsc() >> paymentTypes.sort {it.name}
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: paymentTypeRepository)
        when:
            List<PaymentType> paymentTypesResult = paymentTypeService.findAll()
        then:
            paymentTypesResult != null
            paymentTypesResult.size() == 3
            paymentTypesAre(paymentTypesResult, cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getAguaPersistedPayment(), cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getEndesaPersistedPayment(), cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getGasPersistedPayment())
    }

    def "findBy() should return payment type data"(){
        given:
            List<PaymentType> paymentTypes = new ArrayList<>()
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getEndesaPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getGasPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getAguaPersistedPayment())

        and:
            PaymentType paymentTypeToFind = PaymentTypeUtil.getEndesaPersistedPayment()
        and:
            PaymentTypeRepository paymentTypeRepository = Mock(PaymentTypeRepository)
            paymentTypeRepository.findOne(_) >> paymentTypeToFind
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: paymentTypeRepository)
        when:
            Optional<PaymentType> paymentType = paymentTypeService.findById(paymentTypeToFind.id)
        then:
            paymentTypeIs(paymentType.get(), paymentTypeToFind)
    }

    def "findBy() should return empty if doesn't exist a given payment type"(){
        given:
            List<PaymentType> paymentTypes = new ArrayList<>()
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getEndesaPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getGasPersistedPayment())
            paymentTypes.add(cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getAguaPersistedPayment())

        and:
            PaymentType paymentTypeToFind = PaymentTypeUtil.getEndesaPersistedPayment()
        and:
            PaymentTypeRepository paymentTypeRepository = Mock(PaymentTypeRepository)
            paymentTypeRepository.findOne(_) >> null
            PaymentTypeService paymentTypeService = new RepositoryPaymentTypeService(paymentTypeRepository: paymentTypeRepository)
        when:
            Optional<PaymentType> paymentType = paymentTypeService.findById(paymentTypeToFind.id)
        then:
            assert paymentType.isPresent() == false
    }

    void paymentTypeIs(PaymentType source, PaymentType target) {
        assert source.id == target.id
        assert source.name == target.name
    }

    void paymentTypesAre(List<PaymentType> result, PaymentType... paymentTypes) {
        paymentTypes.eachWithIndex { PaymentType paymentType, int i ->
            assert result.get(i).name == paymentType.name
        }
    }
}
