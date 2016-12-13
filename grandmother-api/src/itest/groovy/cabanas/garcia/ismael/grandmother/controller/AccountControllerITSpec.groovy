package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
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

    def "should return 204 status code when Isma deposits 30.000€"(){
        given: "a person"
            Person person = createPerson("Isma")
        and: "an given account"
            Account account = createDefaultAccount()
        and: "a given amount"
            def amount = new BigDecimal(30.000)
        when: "deposits a given amount"
            String uri = getDepositUri(account)
            def body = getBody(person, amount)
            RequestEntity<DepositRequestBody> requestEntity =
                    RequestEntity.put(serviceURI(uri))
                            .body(body)
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == HttpStatus.NO_CONTENT
    }

    private DepositRequestBody getBody(Person person, BigDecimal amount) {
        new DepositRequestBody(personId: person.id, deposit: amount)
    }

    private String getDepositUri(Account account) {
        UriComponentsBuilder.fromPath(account.id).pathSegment("deposit").build().encode().toUriString()
    }

    Account createDefaultAccount() {
        Account account = new Account(accountNumber: "123123")
        accountRepository.save(account)
        return account
    }

    Person createPerson(String name){
        Person person = new Person(name: name)
        personRepository.save(person)
        return person
    }
}
