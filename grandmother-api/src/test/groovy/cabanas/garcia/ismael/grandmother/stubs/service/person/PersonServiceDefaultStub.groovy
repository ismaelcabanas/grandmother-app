package cabanas.garcia.ismael.grandmother.stubs.service.person

import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.PersonService
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil

class PersonServiceDefaultStub implements PersonService{
    Person person

    protected PersonServiceDefaultStub() {
        this.person = null
    }

    PersonServiceDefaultStub(Person person) {
        this.person = person
    }

    @Override
    Object create(Person person) {
        person.id = PersonUtil.DEFAULT_PERSON_ID
        return person
    }

    @Override
    List<Person> findAll() {
        return null
    }

    @Override
    Object deleteAll() {
        return null
    }

    @Override
    Person findById(Long id) {
        return person
    }

    @Override
    Object delete(Person person) {
        return null
    }

    @Override
    Person update(Person person) {
        return null
    }
}
