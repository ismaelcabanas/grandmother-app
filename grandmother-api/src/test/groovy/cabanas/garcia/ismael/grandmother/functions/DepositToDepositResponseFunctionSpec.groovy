package cabanas.garcia.ismael.grandmother.functions

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import spock.lang.Specification

/**
 * Created by XI317311 on 22/12/2016.
 */
class DepositToDepositResponseFunctionSpec extends Specification{

    private DepositTransactionToDepositResponseFunction function = new DepositTransactionToDepositResponseFunction()

    def "transform deposit object to deposit response object"(){
        given:
            DepositTransaction depositTransaction =
                    new DepositTransaction(amount: 10000, dateOfMovement: DateUtilTest.TODAY,
                        person: PersonUtilTest.getDefaultPerson(), description:  "Transferencia a su favor")
        when:
            DepositResponse depositResponse = function.apply(depositTransaction)
        then:
            depositResponse.amount == depositTransaction.amount
            depositResponse.date == DateUtils.format(depositTransaction.dateOfMovement)
            depositResponse.person.id == depositTransaction.person.id
            depositResponse.person.name == depositTransaction.person.name
            depositResponse.description == depositTransaction.description
    }
}
