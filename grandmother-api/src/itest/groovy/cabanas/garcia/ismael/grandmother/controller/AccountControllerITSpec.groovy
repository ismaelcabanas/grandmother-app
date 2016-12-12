package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.person.Person
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

/**
 * Created by XI317311 on 12/12/2016.
 */
class AccountControllerITSpec extends RestIntegrationBaseSpec{

    @Unroll
    def "should return #statusCodeExpected status code when create a account with number '#accountNumber' and balance #balance" (){
        given:
            Account account = new Account(accountNumber: accountNumber, balance: balance)
            RequestEntity<Account> requestEntity = RequestEntity.post(serviceURI("accounts")).body(account)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        accountNumber | balance | statusCodeExpected
        ""            | 30.000  | HttpStatus.BAD_REQUEST
        null          | 10.000  | HttpStatus.BAD_REQUEST
        "123123"      | 10.000  | HttpStatus.CREATED
        "123123"      | -10.000 | HttpStatus.BAD_REQUEST
    }
}
