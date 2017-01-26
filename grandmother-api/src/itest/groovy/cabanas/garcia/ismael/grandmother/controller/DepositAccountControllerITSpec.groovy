package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.util.AccountITUtil
import cabanas.garcia.ismael.grandmother.util.PersonITUtil
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity

import static cabanas.garcia.ismael.grandmother.utils.DateUtils.*
import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.DepositUtil.*

/**
 * Created by XI317311 on 25/01/2017.
 */
class DepositAccountControllerITSpec extends RestIntegrationBaseSpec {

    @Autowired
    AccountITUtil accountITUtil

    @Autowired
    PersonITUtil personITUtil

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    def "should return deposit transactions with total amount when hits the URL for getting deposits for an account"(){
        given: "an account in the system"
            Account defaultAccount = accountITUtil.createDefault()
        and: "a given person in the system"
            Person defaultPerson = personITUtil.createDefault()
        and: "that person did a deposit of 10000 today on account"
            Deposit depositFromDefaultPersonOf10000AtToday =
                    Deposit.builder()
                        .amount(TEN_THOUSAND)
                        .date(TODAY)
                        .description(DEFAULT_DESCRIPTION)
                        .person(defaultPerson)
                        .build()
            defaultAccount.deposit(depositFromDefaultPersonOf10000AtToday)
        and: "that person did a deposit of 20000 yesterday on account"
            Deposit depositFromDefaultPersonOf20000Yesterday =
                Deposit.builder()
                        .amount(TWENTY_THOUSAND)
                        .date(YESTERDAY)
                        .description(DEFAULT_DESCRIPTION)
                        .person(defaultPerson)
                        .build()
            defaultAccount.deposit(depositFromDefaultPersonOf20000Yesterday)
        and: "persist deposits in the system"
            accountITUtil.update(defaultAccount)
        when: "REST deposits on account url is hit"
            ResponseEntity<DepositsResponse> response =
                    restTemplate.getForEntity(serviceURI("/accounts/$defaultAccount.id/deposits"), DepositsResponse.class)
        then:
            totalAmountDepositsExpected(response.body, 30000)
            responseContainsDeposits(response.body,
                    depositFromDefaultPersonOf20000Yesterday,
                    depositFromDefaultPersonOf10000AtToday)
    }

    void totalAmountDepositsExpected(DepositsResponse response, BigDecimal totalExpected) {
        assert response.total == totalExpected
    }

    void responseContainsDeposits(DepositsResponse response, Deposit... deposits) {
        assert response.deposits.size() == deposits.size()
        response.deposits.eachWithIndex { DepositResponse depositResponse, int i ->
            assert depositResponse.amount == deposits[i].amount
            assert depositResponse.description == deposits[i].description
            assert depositResponse.date == format(deposits[i].date)
            assert depositResponse.person.id == deposits[i].person.id
            assert depositResponse.person.name == deposits[i].person.name
        }
    }

    def deposit(Account account, Deposit deposit) {
        DepositTransaction depositTransaction =
                new DepositTransaction(amount: deposit.amount, dateOfMovement: deposit.date,
                        description: deposit.description, account: account,
                        person: deposit.person)
        depositTransactionRepository.save(depositTransaction)
    }
}
