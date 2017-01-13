package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 22/12/2016.
 */
class PersonUtil {
    static Person getDefaultPerson() {
        return Person.builder().id(1).name("Ismael").build()
    }

    static Person getIsmael() {
        return Person.builder().id(999).name("Ismael").build()
    }

    static Person getBea() {
        return Person.builder().id(998).name("Bea").build()
    }
}
