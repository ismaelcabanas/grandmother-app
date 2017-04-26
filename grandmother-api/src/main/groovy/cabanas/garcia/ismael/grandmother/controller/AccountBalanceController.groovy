package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentsResponse
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService
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

@Slf4j
@RestController()
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class AccountBalanceController {

    @Autowired
    AccountBalanceService balanceService;

    AccountBalanceController(AccountBalanceService balanceService) {
        this.balanceService = balanceService
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/balance", params = ["year","month"])
    ResponseEntity<PaymentsResponse> paymentsByYearAndMonth(@PathVariable("id") long accountId,
                                                            @RequestParam(value = "year", required = true) int year,
                                                            @RequestParam(value = "month", required = true) int month){

        return new ResponseEntity<Void>(HttpStatus.OK)
    }
}
