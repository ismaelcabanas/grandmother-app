package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Person
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
class PersonImpl implements Person {
    private String name
    private PersonService personService

    @Override
    def create() {
        personService.create(this)
    }
}
