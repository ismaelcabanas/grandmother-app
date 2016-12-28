package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.AccountRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.AccountResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.adapters.DepositTransactionToDepositResponseFunction
import cabanas.garcia.ismael.grandmother.service.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.validation.Valid
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Created by XI317311 on 12/12/2016.
 */
@Slf4j
@RestController()
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class AccountController {

    @Autowired
    AccountService accountService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> create(@Valid @RequestBody AccountRequestBody accountRequestBody){
        log.debug("Creating account $accountRequestBody")
        Account account = accountService.open(accountRequestBody.accountNumber, accountRequestBody.balance)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8)
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/accounts/" + account.id)
                .buildAndExpand(account.id).toUri())

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED)
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/deposit")
    ResponseEntity<Void> deposit(@PathVariable("id") Long accountId, @Valid @RequestBody DepositRequestBody requestBody){
        log.debug("Updating account with data $requestBody")
        Deposit deposit = Deposit.builder()
                .amount(requestBody.deposit)
                .date(requestBody.dateOfDeposit)
                .person(Person.builder().id(requestBody.personId).build())
                .description(requestBody.description)
                .build()
        accountService.deposit(accountId, deposit)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8)

        return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT)
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/payment")
    ResponseEntity<Void> payment(@PathVariable("id") Long accountId, @Valid @RequestBody PaymentRequestBody requestBody){
        log.debug("Updating account with data $requestBody")

        Payment payment = Payment.builder()
            .amount(requestBody.amount)
            .date(requestBody.dateOfPayment)
            .type(PaymentType.builder().id(requestBody.paymentTypeId).build())
            .description(requestBody.description)
            .build()

        accountService.payment(accountId, payment)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8)

        return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<AccountResponse> read(@PathVariable("id") Long accountId){
        log.debug("Getting account $accountId")

        Account account = accountService.get(accountId)

        AccountResponse accountResponse = AccountResponse.builder()
                .balance(account.balance)
                .accountNumber(account.accountNumber)
                .id(account.id)
                .build()

        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits")
    ResponseEntity<DepositsResponse> deposits(@PathVariable("id") Long accountId){
        log.debug("Getting depositTransactions on account $accountId")

        Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactions(accountId)

        BigDecimal totalOfDeposits = totalOfDepositTransactions(depositTransactions)

        DepositsResponse depositsResponse = DepositsResponse.builder()
            .total(totalOfDeposits)
            .deposits(transform(depositTransactions))
            .build()

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

    private Collection<DepositResponse> transform(Collection<DepositTransaction> depositTransactions) {
        assert depositTransactions != null
        Function depositToDepositResponse = new DepositTransactionToDepositResponseFunction()
        return depositTransactions.stream().map(depositToDepositResponse).collect(Collectors.<DepositResponse>toList())
    }

    private BigDecimal totalOfDepositTransactions(Collection<DepositTransaction> transactions) {
        assert transactions != null
        BinaryOperator<BigDecimal> addOperator = {s1, s2 -> s1.add(s2)}
        BigDecimal total = transactions.stream()
                .filter(Objects.&nonNull) // de la colleción, no cojas los objetos nulos. En Groovy el operador .& equivale a :: en Java
                .filter({transaction -> transaction.amount != null}) // de cada elemento de la colección, no cojas aquellos cuyo atributo amount sea nulo
                .map({transaction -> transaction.amount}) // de la colección, mapeamela a una colección de objetos amount
                .reduce(BigDecimal.ZERO, addOperator) // aplica el operador a cada elemento de la colección para reducirmelo a un valor
        return total
    }
}
