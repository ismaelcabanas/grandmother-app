package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 22/12/2016.
 */
class PersonUtil {

    public static final String DEFAULT_PERSON_NAME = "Ismael"
    public static final Long DEFAULT_PERSON_ID = 1L

    static Person getDefaultPersonPersisted() {
        return Person.builder().id(DEFAULT_PERSON_ID).name(DEFAULT_PERSON_NAME).build()
    }

    static Person getPersistedIsmael() {
        return Person.builder().id(999).name(DEFAULT_PERSON_NAME).build()
    }

    static Person getPersistedBea() {
        return Person.builder().id(998).name("Bea").build()
    }

    static Person getIsmael() {
        Person.builder().name(DEFAULT_PERSON_NAME).build()
    }

    static Person getBea() {
        return Person.builder().name("Bea").build()
    }

    static Person getDefaultPerson(){
        Person.builder().name(DEFAULT_PERSON_NAME).build()
    }
}
