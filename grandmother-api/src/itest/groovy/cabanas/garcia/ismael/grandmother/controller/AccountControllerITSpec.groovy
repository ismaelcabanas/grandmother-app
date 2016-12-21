package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Unroll

/**
 * Created by XI317311 on 12/12/2016.
 */
class AccountControllerITSpec extends RestIntegrationBaseSpec{

    @Autowired
    PersonRepository personRepository

    @Autowired
    AccountRepository accountRepository

    @Autowired
    ChargeTypeRepository chargeTypeRepository

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
        Account account = new Account(accountNumber: "123123")
        accountRepository.save(account)
        return account
    }

    def persistPerson(Person person){
        personRepository.save(person)
    }
}
