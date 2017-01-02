package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
@RequestMapping(value = "/paymentTypes")
class PaymentTypeController {

    public static final String PAYMENT_TYPE_BASE_PATH = "/paymentTypes"

    @Autowired
    PaymentTypeService paymentTypeService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> create(@Valid @RequestBody PaymentType paymentType){
        log.debug("Creating... $paymentType")
        paymentTypeService.create(paymentType)
        new ResponseEntity<Void>(HttpStatus.CREATED)
    }

}
