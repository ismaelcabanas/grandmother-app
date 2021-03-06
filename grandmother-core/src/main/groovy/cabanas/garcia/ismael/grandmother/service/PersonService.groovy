package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.person.Person

/**
 * Created by XI317311 on 05/12/2016.
 */
interface PersonService {

    def create(Person person)

    List<Person> findAll()

    def deleteAll()

    Person findById(Long id)

    def delete(Person person)

    Person update(Person person)
}
