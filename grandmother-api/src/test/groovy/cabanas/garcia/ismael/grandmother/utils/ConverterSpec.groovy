package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.adapters.DepositTransactionToDepositResponseFunction
import spock.lang.Specification

/**
 * Created by XI317311 on 22/12/2016.
 */
class ConverterSpec extends Specification{

    def "convert a collection of deposit objects to collection of deposit response objects"(){
        given:
            DepositTransaction deposit5000 = new DepositTransaction(amount: 5000, dateOfMovement: DateUtilTest.TODAY)
            DepositTransaction deposit10000 = new DepositTransaction(amount: 10000, dateOfMovement: DateUtilTest.TODAY)
            Collection<DepositTransaction> transactions = new ArrayList<DepositTransaction>()
            transactions.add(deposit5000)
            transactions.add(deposit10000)
        when:
            Collection<DepositResponse> depositResponses = Converter.convert(transactions, new DepositTransactionToDepositResponseFunction())
        then:
            depositResponses.size() == 2
            assert depositResponses.contains(expectedDepositResponse(deposit5000))
            assert depositResponses.contains(expectedDepositResponse(deposit10000))
    }

    DepositResponse expectedDepositResponse(DepositTransaction deposit) {
        new DepositTransactionToDepositResponseFunction().apply(deposit)
    }
}
