package cabanas.garcia.ismael.grandmother.domain.person.repository

import cabanas.garcia.ismael.grandmother.domain.person.Person
import org.springframework.data.repository.CrudRepository

/**
 * Created by XI317311 on 07/12/2016.
 */
interface PersonRepository extends CrudRepository<Person, String> {

}