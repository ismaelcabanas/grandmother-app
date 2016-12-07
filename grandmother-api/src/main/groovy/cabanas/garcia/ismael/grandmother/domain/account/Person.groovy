package cabanas.garcia.ismael.grandmother.domain.account

import cabanas.garcia.ismael.grandmother.service.PersonService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class Person {

    private String name
    private PersonService personService

    String getName() {
        name
    }

    def create() {
        personService.create(this)
    }
}