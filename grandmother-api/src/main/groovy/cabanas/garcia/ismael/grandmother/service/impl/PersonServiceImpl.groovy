package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.model.Person
import cabanas.garcia.ismael.grandmother.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.PersonService

/**
 * Created by XI317311 on 05/12/2016.
 */
class PersonServiceImpl implements PersonService {

    private PersonRepository repository

    @Override
    def create(Person person) {
        return null
    }

    @Override
    def deleteAll() {
        return null
    }

    @Override
    List<Person> findAll() {
        return null
    }
}
