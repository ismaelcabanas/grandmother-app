package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.adapter.TransactionsAdapter
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.service.DepositAccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by XI317311 on 30/12/2016.
 */
@Slf4j
@RestController()
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class DepositsAccountController {

    @Autowired
    DepositAccountService depositAccountService

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits")
    ResponseEntity<DepositsResponse> deposits(@PathVariable("id") Long accountId){
        log.debug("Getting depositTransactions on account $accountId")

        Transactions depositTransactions = depositAccountService.getDepositTransactions(accountId)

        log.debug("Deposit transactions returned $depositTransactions")

        DepositsResponse depositsResponse = TransactionsAdapter.mapDepositTransactionsEntityToResponse(depositTransactions)

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits", params = "person_id")
    ResponseEntity<DepositsResponse> depositsByPerson(@PathVariable("id") Long accountId,
                                                      @RequestParam(value = "person_id", required = true) Long personId){
        log.debug("Getting depositTransactions by person on account $accountId and person identifier $personId")

        Transactions depositTransactions = depositAccountService.getDepositTransactionsByPersonId(accountId, personId)

        log.debug("Deposit transactions returned $depositTransactions")

        DepositsResponse depositsResponse = TransactionsAdapter.mapDepositTransactionsEntityToResponse(depositTransactions)

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deposits", params = ["person_id","year"])
    ResponseEntity<DepositsResponse> depositsByPersonAndYear(@PathVariable("id") Long accountId,
                                                        @RequestParam(value = "person_id", required = true) Long personId,
                                                        @RequestParam(value = "year", required = true) int year){
        log.debug("Getting depositTransactions by person and year on account $accountId for person identifier $personId " +
                "and year $year")

        Transactions depositTransactions = depositAccountService.getDepositTransactionsByPersonIdAndYear(accountId, personId, year)

        log.debug("Deposit transactions returned $depositTransactions")

        DepositsResponse depositsResponse = TransactionsAdapter.mapDepositTransactionsEntityToResponse(depositTransactions)

        return new ResponseEntity<DepositsResponse>(depositsResponse, HttpStatus.OK)
    }

}
