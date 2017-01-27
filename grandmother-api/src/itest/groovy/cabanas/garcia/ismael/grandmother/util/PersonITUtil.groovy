package cabanas.garcia.ismael.grandmother.util

import cabanas.garcia.ismael.grandmother.Profiles
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Created by XI317311 on 25/01/2017.
 */
@Configuration
@Profile(Profiles.INTEGRATION)
class PersonITUtil {

    @Autowired
    PersonRepository personRepository

    Person createDefault(){
        personRepository.save(new Person(name: PersonUtil.DEFAULT_PERSON_NAME))
    }

    def static create(TestEntityManager entityManager, String name) {
        entityManager.persist(new Person(name: name))
    }
}
