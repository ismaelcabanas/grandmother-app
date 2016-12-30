package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.adapters.DepositTransactionToDepositResponseFunction
import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.service.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Created by XI317311 on 30/12/2016.
 */
@Slf4j
@RestController()
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class DepositsAccountController {

    @Autowired
    AccountService accountService

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits")
    ResponseEntity<DepositsResponse> deposits(@PathVariable("id") Long accountId){
        log.debug("Getting depositTransactions on account $accountId")

        Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactions(accountId)

        log.debug("Deposit transactions returned $depositTransactions")

        BigDecimal totalOfDeposits = totalOfDepositTransactions(depositTransactions)

        DepositsResponse depositsResponse = DepositsResponse.builder()
                .total(totalOfDeposits)
                .deposits(transform(depositTransactions))
                .build()

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits", params = "person_id")
    ResponseEntity<DepositsResponse> depositsByPerson(@PathVariable("id") Long accountId,
                                                      @RequestParam(value = "person_id", required = true) Long personId){
        log.debug("Getting depositTransactions on account $accountId")

        Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactionsByPersonId(accountId, personId)

        log.debug("Deposit transactions returned $depositTransactions")

        BigDecimal totalOfDeposits = totalOfDepositTransactions(depositTransactions)

        DepositsResponse depositsResponse = DepositsResponse.builder()
                .total(totalOfDeposits)
                .deposits(transform(depositTransactions))
                .build()

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits", params = "year")
    ResponseEntity<DepositsResponse> depositsByYear(@PathVariable("id") Long accountId,
                                                      @RequestParam(value = "year", required = true) int year){
        log.debug("Getting depositTransactions on account $accountId")

        Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactionsByYear(accountId, year)

        log.debug("Deposit transactions returned $depositTransactions")

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
        BinaryOperator<BigDecimal> addOperator = { s1, s2 -> s1.add(s2)}
        BigDecimal total = transactions.stream()
                .filter(Objects.&nonNull) // de la colleción, no cojas los objetos nulos. En Groovy el operador .& equivale a :: en Java
                .filter({transaction -> transaction.amount != null}) // de cada elemento de la colección, no cojas aquellos cuyo atributo amount sea nulo
                .map({transaction -> transaction.amount}) // de la colección, mapeamela a una colección de objetos amount
                .reduce(BigDecimal.ZERO, addOperator) // aplica el operador a cada elemento de la colección para reducirmelo a un valor
        return total
    }

}
