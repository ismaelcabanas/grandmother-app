package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 05/12/2016.
 */
@Service
class PersonServiceImpl implements PersonService {

    @Autowired
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
