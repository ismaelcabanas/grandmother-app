package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.AccountRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

/**
 * Created by XI317311 on 12/12/2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/accounts")
class AccountController {

    @Autowired
    AccountService accountService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> create(@Valid @RequestBody AccountRequestBody accountRequestBody){
        log.debug("Creating account $accountRequestBody")
        accountService.open(accountRequestBody.accountNumber, accountRequestBody.balance)
        return new ResponseEntity<Void>(HttpStatus.CREATED)
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/deposit")
    ResponseEntity<Void> deposit(@PathVariable("id") String accountId, @Valid @RequestBody DepositRequestBody requestBody){
        log.debug("Updating account with data $requestBody")
        Deposit deposit = Deposit.builder()
                .amount(requestBody.deposit)
                .date(requestBody.dateOfDeposit)
                .person(Person.builder().id(requestBody.personId).build())
                .description(requestBody.description)
                .build()
        accountService.deposit(accountId, deposit)
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/payment")
    ResponseEntity<Void> payment(@PathVariable("id") String accountId, @Valid @RequestBody PaymentRequestBody requestBody){
        log.debug("Updating account with data $requestBody")
        Payment payment = Payment.builder()
            .amount(requestBody.amount)
            .date(requestBody.dateOfPayment)
            .type(PaymentType.builder().id(requestBody.paymentTypeId).build())
            .description(requestBody.description)
            .build()
        accountService.payment(accountId, payment)
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<AccountResponse> read(@PathVariable("id") String accountId){
        log.debug("Getting account $accountId")

        Account account = accountService.get(accountId)

        AccountResponse accountResponse = AccountResponse.builder()
                .balance(account.balance)
                .accountNumber(account.accountNumber)
                .id(account.id)
                .build()

        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK)
    }
}
