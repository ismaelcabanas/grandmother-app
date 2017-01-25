package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 22/12/2016.
 */
class PersonUtil {

    public static final String DEFAULT_PERSON_NAME = "Ismael"

    static Person getDefaultPersistedPerson() {
        return Person.builder().id(1).name(DEFAULT_PERSON_NAME).build()
    }

    static Person getPersistedIsmael() {
        return Person.builder().id(999).name(DEFAULT_PERSON_NAME).build()
    }

    static Person getBea() {
        return Person.builder().id(998).name("Bea").build()
    }
}
