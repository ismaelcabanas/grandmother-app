package cabanas.garcia.ismael.grandmother.controller

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
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity

/**
 * Created by XI317311 on 25/01/2017.
 */
class DepositAccountControllerITSpec extends RestIntegrationBaseSpec {

    @Autowired
    AccountRepository accountRepository

    @Autowired
    PersonRepository personRepository

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    def "should return deposit transactions with total amount when hits the URL for getting deposits for an account"(){
        given: "an account in the system"
            Account account = AccountITUtil.createDefault(accountRepository)
        and: "a given person in the system"
            Person person = PersonITUtil.createDefault(personRepository)
        and: "that person does two deposits on account"
            Deposit deposit10000 = new Deposit(amount: 10000, date: DateUtil.TODAY, description: "Transferencia a su favor", person: person)
            Deposit deposit20000 = new Deposit(amount: 20000, date: DateUtil.YESTERDAY, description: "Transferencia a su favor", person: person)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        when: "REST deposits on account url is hit"
            ResponseEntity<DepositsResponse> response =
                    restTemplate.getForEntity(serviceURI("/accounts/$account.id/deposits"), DepositsResponse.class)
        then:
            totalAmountDepositsExpected(response.body, 30000)
            responseContainsDeposits(response.body, deposit10000, deposit20000)
    }

    def totalAmountDepositsExpected(DepositsResponse response, BigDecimal totalExpected) {
        response.total == totalExpected
    }

    def responseContainsDeposits(DepositsResponse response, Deposit... deposits) {
        response.deposits.size() == deposits.size()
        response.deposits.forEach({depositResponse ->
            deposits.contains(new Deposit(
                    amount: depositResponse.amount,
                    date: depositResponse.date,
                    description: depositResponse.description,
                    person: new Person(name: depositResponse.person.name)))
        })
    }

    def deposit(Account account, Deposit deposit) {
        DepositTransaction depositTransaction =
                new DepositTransaction(amount: deposit.amount, dateOfMovement: deposit.date,
                        description: deposit.description, account: account,
                        person: deposit.person)
        depositTransactionRepository.save(depositTransaction)
    }
}
