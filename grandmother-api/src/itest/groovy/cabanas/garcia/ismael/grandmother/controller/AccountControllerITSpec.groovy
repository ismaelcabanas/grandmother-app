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
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Unroll

import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.*

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


    @Unroll
    def "should return status #statusCodeExpected when hits URL for creating an account with account number '#accountNumber' and balance #balance" (){
        given:
            Account account = new Account(accountNumber: accountNumber, balance: balance)
            RequestEntity<Account> requestEntity = RequestEntity.post(serviceURI("/accounts")).body(account)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        accountNumber | balance          | statusCodeExpected
        ""            | THIRTY_THOUSAND  | HttpStatus.BAD_REQUEST
        null          | THIRTY_THOUSAND  | HttpStatus.BAD_REQUEST
        "123123"      | ZERO             | HttpStatus.CREATED
        "123123"      | -THIRTY_THOUSAND | HttpStatus.CREATED
    }

    @Unroll
    def "should return status #statusCodeExpected when hits URL for depositing #amount€ on an account at #date"(){
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
        amount          | person                   | date                    | description     | statusCodeExpected
        THIRTY_THOUSAND | new Person(name: "Isma") | parseDate("01/01/2010") | "Transferencia" | HttpStatus.NO_CONTENT
        null            | new Person(name: "Isma") | parseDate("01/01/2010") | "Transferencia" | HttpStatus.BAD_REQUEST
        THIRTY_THOUSAND | new Person(name: "Isma") | null                    | "Transferencia" | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "should return status #statusCodeExpected when hits URL for charging a of #amount€ at #date"(){
        given: "a given account"
            Account account = openDefaultAccount()
        and: "a payment type in the system"
            persistChargeType(paymentType)
        when: "does a payment on given account"
            def paymentRequestBody = getBody(paymentType, amount, date, description)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI("/accounts/$account.id/payment")).body(paymentRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount          | paymentType                   | date                    | description | statusCodeExpected
        THIRTY_THOUSAND | new PaymentType(name: "Agua") | parseDate("01/01/2010") | "Agua"      | HttpStatus.NO_CONTENT
        null            | new PaymentType(name: "Agua") | parseDate("01/01/2010") | "Agua"      | HttpStatus.BAD_REQUEST
        THIRTY_THOUSAND | new PaymentType(name: "Agua") | null                    | "Agua"      | HttpStatus.BAD_REQUEST

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
        return accountRepository.save(Account.builder()
                .accountNumber(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
                .balance(AccountUtil.DEFAULT_BALANCE)
                .build())
    }

    def persistPerson(Person person){
        personRepository.save(person)
    }
}
