package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.adapter.PaymentTypeAdapter
import cabanas.garcia.ismael.grandmother.controller.adapter.PersonAdapter
import cabanas.garcia.ismael.grandmother.controller.request.PersonRequestBody
import cabanas.garcia.ismael.grandmother.controller.response.PaymentTypeResponse
import cabanas.garcia.ismael.grandmother.controller.response.PersonResponse
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.PersonService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.validation.Valid

@Slf4j
@RestController
@RequestMapping(value = "/persons")
class PersonController {

    public static String PERSON_BASE_PATH = "/persons"

    @Autowired
    PersonService personService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> create(@Valid @RequestBody PersonRequestBody personRequestBody){
        Person person = PersonAdapter.mapToPerson(personRequestBody)

        Person newPerson = personService.create(person)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8)
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/persons/" + newPerson.id)
                .buildAndExpand(newPerson.id).toUri())

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED)
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<?> read(@PathVariable("id") Long personId){

        Person person = personService.findById(personId)

        log.debug("Person entity returned by person service $person")

        ResponseEntity<?> responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND)
        if(person != null){
            PersonResponse personResponse = PersonAdapter.mapPersonEntityToResponse(person)
            responseEntity = new ResponseEntity<>(personResponse, HttpStatus.OK)
        }

        return responseEntity

    }
}
