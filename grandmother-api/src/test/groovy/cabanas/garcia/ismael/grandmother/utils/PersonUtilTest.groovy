package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 22/12/2016.
 */
class PersonUtilTest {
    static Person getDefaultPerson() {
        return Person.builder().id("1").name("Ismael").build()
    }
}
