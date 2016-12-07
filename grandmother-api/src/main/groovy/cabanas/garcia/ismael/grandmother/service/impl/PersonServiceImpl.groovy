package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.PersonService

/**
 * Created by XI317311 on 05/12/2016.
 */
class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository

    @Override
    def create(Person person) {
        personRepository.save(person)
    }

    @Override
    def deleteAll() {
        personRepository.deleteAll()
    }

    @Override
    Person findById(String id) {
        personRepository.findOne(id)
    }

    @Override
    def delete(Person person) {
        personRepository.delete(person)
    }

    @Override
    Person update(Person person) {
        personRepository.save(person)
    }

    @Override
    List<Person> findAll() {
        return personRepository.findAll()
    }
}
