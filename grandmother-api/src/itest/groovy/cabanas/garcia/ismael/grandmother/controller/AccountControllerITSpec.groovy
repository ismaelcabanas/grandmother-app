package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTypeRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Unroll

/**
 * Created by XI317311 on 12/12/2016.
 */
//@Transactional
class AccountControllerITSpec extends RestIntegrationBaseSpec{

    @Autowired
    AccountRepository accountRepository

    @Autowired
    PersonRepository personRepository

    @Autowired
    PaymentTypeRepository chargeTypeRepository

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    @Unroll
    def "should return status #statusCodeExpected when create a account with account number '#accountNumber' and balance #balance" (){
        given:
            Account account = new Account(accountNumber: accountNumber, balance: balance)
            RequestEntity<Account> requestEntity = RequestEntity.post(serviceURI("/accounts")).body(account)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        accountNumber | balance                 | statusCodeExpected
        ""            | new BigDecimal(30.000)  | HttpStatus.BAD_REQUEST
        null          | new BigDecimal(30.000)  | HttpStatus.BAD_REQUEST
        "123123"      | new BigDecimal(0)       | HttpStatus.CREATED
        "123123"      | new BigDecimal(-30.000) | HttpStatus.CREATED
    }

    @Unroll
    def "should return status #statusCodeExpected when #person.name deposits #amount€ at #date"(){
        given: "an given account"
            Account account = openDefaultAccount()
        and: "a person in the system"
            persistPerson(person)
        when: "deposits a given amount"
            def depositRequestBody = getBody(person, amount, date, description)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI("/accounts/$account.id/deposit")).body(depositRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount                | person                   | date                    | description     | statusCodeExpected
        new BigDecimal(30000) | new Person(name: "Isma") | parseDate("01/01/2010") | "Transferencia" | HttpStatus.NO_CONTENT
        null                  | new Person(name: "Isma") | parseDate("01/01/2010") | "Transferencia" | HttpStatus.BAD_REQUEST
        new BigDecimal(30000) | new Person(name: "Isma") | null                    | "Transferencia" | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "should return status #statusCodeExpected when there is a payment #paymentType.name of #amount€ at #date"(){
        given: "a given account"
            Account account = openDefaultAccount()
        and: "an amount type in the system"
            persistChargeType(paymentType)
        when: "deposits a given amount"
            def depositRequestBody = getBody(paymentType, amount, date, description)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI("/accounts/$account.id/payment")).body(depositRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount                | paymentType                   | date                    | description | statusCodeExpected
        new BigDecimal(30000) | new PaymentType(name: "Agua") | parseDate("01/01/2010") | "Agua"      | HttpStatus.NO_CONTENT
        null                  | new PaymentType(name: "Agua") | parseDate("01/01/2010") | "Agua"      | HttpStatus.BAD_REQUEST
        new BigDecimal(30000) | new PaymentType(name: "Agua") | null                    | "Agua"      | HttpStatus.BAD_REQUEST

    }

    def "should return account data when hits the URL for getting account by identifier"(){
        given: "a given account"
            Account account = openDefaultAccount()
        when: "REST account get url is hit"
            ResponseEntity<AccountResponse> response =
                    restTemplate.getForEntity(serviceURI("/accounts/$account.id"), AccountResponse.class)
        then:
            AccountResponse accountResponse = response.body
            accountResponse.accountNumber == account.accountNumber
            accountResponse.balance == account.balance
    }

    def "should return deposit transactions and total for an account"(){
        given: "an account in the system"
            Account account = openDefaultAccount()
        and: "a given person in the system"
            Person person = persistPerson(new Person(name: "Ismael"))
        and: "that person does two deposits on account"
            Deposit deposit10000 = new Deposit(amount: 10000, date: DateUtilTest.TODAY, description: "Transferencia a su favor", person: person)
            Deposit deposit20000 = new Deposit(amount: 20000, date: DateUtilTest.YESTERDAY, description: "Transferencia a su favor", person: person)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        when: "REST deposits on account url is hit"
            ResponseEntity<DepositsResponse> response =
                restTemplate.getForEntity(serviceURI("/accounts/$account.id/deposits"), DepositsResponse.class)
        then:
            totalAmountDepositsExpected(response.body, 30000)
            responseContainsDeposits(response.body, deposit10000, deposit20000)
    }

    def deposit(Account account, Deposit deposit) {
        DepositTransaction depositTransaction =
                new DepositTransaction(amount: deposit.amount, dateOfMovement: deposit.date,
                    description: deposit.description, account: account,
                    person: deposit.person)
        depositTransactionRepository.save(depositTransaction)
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

    def totalAmountDepositsExpected(DepositsResponse response, BigDecimal totalExpected) {
        response.total == totalExpected
    }

    String getChargeUri(Account account) {
        UriComponentsBuilder.fromPath(account.id).pathSegment("amount").build().encode().toUriString()
    }

    def persistChargeType(PaymentType chargeType) {
        chargeTypeRepository.save(chargeType)
    }

    Date parseDate(String date) {
        Date.parse("dd/MM/yyyy", date)
    }

    private DepositRequestBody getBody(Person person, BigDecimal amount, Date date, String description) {
        new DepositRequestBody(personId: person.id, deposit: amount, dateOfDeposit: date, description: description)
    }

    private PaymentRequestBody getBody(PaymentType chargeType, BigDecimal amount, Date date, String description) {
        new PaymentRequestBody(paymentTypeId: chargeType.id, amount: amount, dateOfPayment: date, description: description)
    }

    private String getDepositUri(Account account) {
        UriComponentsBuilder.fromPath(account.id).pathSegment("deposit").build().encode().toUriString()
    }

    Account openDefaultAccount() {
        return accountRepository.save(new Account(accountNumber: "123"))
    }

    def persistPerson(Person person){
        personRepository.save(person)
    }
}
