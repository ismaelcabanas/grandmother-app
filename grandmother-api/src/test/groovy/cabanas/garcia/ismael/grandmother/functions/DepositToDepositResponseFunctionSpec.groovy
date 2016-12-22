package cabanas.garcia.ismael.grandmother.functions

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import spock.lang.Specification

/**
 * Created by XI317311 on 22/12/2016.
 */
class DepositToDepositResponseFunctionSpec extends Specification{

    private DepositToDepositResponseFunction function = new DepositToDepositResponseFunction()

    def "transform deposit object to deposit response object"(){
        given:
            Deposit deposit = Deposit.builder()
                    .amount(10000)
                    .date(DateUtilTest.TODAY)
                    .person(PersonUtilTest.getDefaultPerson())
                    .description("Transferencia a su favor")
                    .build()
        when:
            DepositResponse depositResponse = function.apply(deposit)
        then:
            depositResponse.amount == deposit.amount
            depositResponse.date == deposit.date
            depositResponse.person.id == deposit.person.id
            depositResponse.person.name == deposit.person.name
            depositResponse.description == deposit.description
    }
}
