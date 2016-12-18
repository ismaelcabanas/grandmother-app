package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.AccountRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
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
        accountService.deposit(accountId, requestBody.personId, requestBody.deposit, requestBody.dateOfDeposit)
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/payment")
    ResponseEntity<Void> payment(@PathVariable("id") String accountId, @Valid @RequestBody PaymentRequestBody requestBody){
        log.debug("Updating account with data $requestBody")
        accountService.payment(accountId, requestBody.paymentTypeId, requestBody.amount, requestBody.dateOfPayment)
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
