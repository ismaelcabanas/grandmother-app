package cabanas.garcia.ismael.grandmother.controller.adapter

import cabanas.garcia.ismael.grandmother.controller.response.PersonResponse
import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 08/01/2017.
 */
class PersonAdapter {
    static PersonResponse mapPersonEntityToResponse(Person person) {
        PersonResponse.builder()
            .id(person.id)
            .name(person.name)
            .build()
    }
}
