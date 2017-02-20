package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.adapters.DepositTransactionToDepositResponseFunction
import cabanas.garcia.ismael.grandmother.controller.adapter.TransactionsAdapter
import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentsResponse
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.service.PaymentAccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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
 * Created by XI317311 on 04/01/2017.
 */
@Slf4j
@RestController()
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class PaymentsAccountController {

    @Autowired
    PaymentAccountService paymentAccountService

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/payments", params = ["year","month"])
    ResponseEntity<PaymentsResponse> paymentsByYearAndMonth(@PathVariable("id") long accountId,
                                                            @RequestParam(value = "year", required = true) int year,
                                                            @RequestParam(value = "month", required = true) int month){

        log.debug("Getting payment transactions by year and month on account $accountId for year $year and month $month")

        Transactions paymentTransactions =
                paymentAccountService.getPaymentTransactionsByYearAndMonth(accountId, year, month)

        log.debug("Payment transactions returned $paymentTransactions")

        PaymentsResponse paymentsResponse = TransactionsAdapter.mapPaymentTransactionsEntityToResponse(paymentTransactions)

        return new ResponseEntity<PaymentsResponse>(paymentsResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/payments", params = ["year"])
    ResponseEntity<PaymentsResponse> paymentsByYear(@PathVariable("id") long accountId,
                                                            @RequestParam(value = "year", required = true) int year){

        log.debug("Getting payment transactions by year on account $accountId for year $year")

        Transactions paymentTransactions =
                paymentAccountService.getPaymentTransactionsByYear(accountId, year)

        log.debug("Payment transactions returned $paymentTransactions")

        PaymentsResponse paymentsResponse = TransactionsAdapter.mapPaymentTransactionsEntityToResponse(paymentTransactions)

        return new ResponseEntity<PaymentsResponse>(paymentsResponse, HttpStatus.OK)
    }

    BigDecimal totalOfPaymentTransactions(Collection<PaymentTransaction> transactions) {
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
