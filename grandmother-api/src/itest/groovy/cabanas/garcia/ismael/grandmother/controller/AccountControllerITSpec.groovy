package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.ChargeRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
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
        "123123"      | new BigDecimal(-30.000) | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "should return status #statusCodeExpected when #person.name deposits #amount€ at #date"(){
        given: "an given account"
            Account account = openDefaultAccount()
        and: "a person in the system"
            persistPerson(person)
        when: "deposits a given amount"
            def depositRequestBody = getBody(person, amount, date)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI("/accounts/$account.id/deposit")).body(depositRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount                | person                   | date                    | statusCodeExpected
        new BigDecimal(30000) | new Person(name: "Isma") | parseDate("01/01/2010") | HttpStatus.NO_CONTENT
        null                  | new Person(name: "Isma") | parseDate("01/01/2010") | HttpStatus.BAD_REQUEST
        new BigDecimal(30000) | new Person(name: "Isma") | null                    | HttpStatus.BAD_REQUEST

    }

    @Unroll
    def "should return status #statusCodeExpected when there is a charge #chargeType.name of #amount€ at #date"(){
        given: "an given account"
            Account account = openDefaultAccount()
        and: "a charge type in the system"
            persistChargeType(chargeType)
        when: "deposits a given amount"
            def depositRequestBody = getBody(chargeType, amount, date)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI("/accounts/$account.id/charge")).body(depositRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount                | chargeType                   | date                    | statusCodeExpected
        new BigDecimal(30000) | new ChargeType(name: "Agua") | parseDate("01/01/2010") | HttpStatus.NO_CONTENT
        null                  | new ChargeType(name: "Agua") | parseDate("01/01/2010") | HttpStatus.BAD_REQUEST
        new BigDecimal(30000) | new ChargeType(name: "Agua") | null                    | HttpStatus.BAD_REQUEST

    }


    String getChargeUri(Account account) {
        UriComponentsBuilder.fromPath(account.id).pathSegment("charge").build().encode().toUriString()
    }

    def persistChargeType(ChargeType chargeType) {
        chargeTypeRepository.save(chargeType)
    }

    Date parseDate(String date) {
        Date.parse("dd/MM/yyyy", date)
    }

    private DepositRequestBody getBody(Person person, BigDecimal amount, Date date) {
        new DepositRequestBody(personId: person.id, deposit: amount, dateOfDeposit: date)
    }

    private ChargeRequestBody getBody(ChargeType chargeType, BigDecimal amount, Date date) {
        new ChargeRequestBody(chargeTypeId: chargeType.id, charge: amount, dateOfCharge: date)
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
