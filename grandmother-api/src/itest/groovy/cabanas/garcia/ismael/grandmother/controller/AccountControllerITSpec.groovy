package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
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

    @Override
    String getBasePath() {
        return "accounts/"
    }
    @Autowired
    PersonRepository personRepository

    @Autowired
    AccountRepository accountRepository

    @Unroll
    def "should return #statusCodeExpected status code when create a account with number '#accountNumber' and balance #balance" (){
        given:
            Account account = new Account(accountNumber: accountNumber, balance: balance)
            RequestEntity<Account> requestEntity = RequestEntity.post(serviceURI()).body(account)
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
    def "should return #statusCodeExpected status code when #person.name deposits #amount€ at #date"(){
        given: "an given account"
            Account account = openDefaultAccount()
        and: "a person in the system"
            persistPerson(person)
        when: "deposits a given amount"
            String uri = getDepositUri(account)
            def depositRequestBody = getBody(person, amount, date)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI(uri)).body(depositRequestBody)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        amount                | person                   | date                    | statusCodeExpected
        new BigDecimal(30000) | new Person(name: "Isma") | parseDate("01/01/2010") | HttpStatus.NO_CONTENT
        null | new Person(name: "Isma") | parseDate("01/01/2010") | HttpStatus.BAD_REQUEST
        new BigDecimal(30000) | new Person(name: "Isma") | null | HttpStatus.BAD_REQUEST

    }

    Date parseDate(String date) {
        Date.parse("dd/MM/yyyy", date)
    }

    private DepositRequestBody getBody(Person person, BigDecimal amount, Date date) {
        new DepositRequestBody(personId: person.id, deposit: amount, dateOfDeposit: date)
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
