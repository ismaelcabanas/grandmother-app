package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.adapter.PaymentTypeAdapter
import cabanas.garcia.ismael.grandmother.controller.response.PaymentTypesResponse
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.validation.Valid

/**
 * Created by XI317311 on 12/12/2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/paymentTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class PaymentTypeController {

    public static final String PAYMENT_TYPE_BASE_PATH = "/paymentTypes"

    @Autowired
    PaymentTypeService paymentTypeService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> create(@Valid @RequestBody PaymentType paymentType){
        log.debug("Creating... $paymentType")
        PaymentType paymentTypeBo = paymentTypeService.create(paymentType)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8)
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/paymentTypes/" + paymentTypeBo.id)
                .buildAndExpand(paymentTypeBo.id).toUri())

        new ResponseEntity<Void>(headers, HttpStatus.CREATED)
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<PaymentTypesResponse> readAll(){
        List<PaymentType> paymentTypes = paymentTypeService.findAll()

        log.debug("Payment Types entities returned by payment type service $paymentTypes")

        PaymentTypesResponse paymentTypeResponse = PaymentTypeAdapter.mapEntitiesToResponse(paymentTypes)

        log.debug("Payment type response $paymentTypeResponse")

        new ResponseEntity<PaymentTypesResponse>(paymentTypeResponse, HttpStatus.OK)
    }


}
