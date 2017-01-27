package cabanas.garcia.ismael.grandmother.util

import cabanas.garcia.ismael.grandmother.domain.person.Person
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

/**
 * Created by XI317311 on 27/01/2017.
 */
class TestEntityManagerUtil {

    TestEntityManager testEntityManager

    def <T> T persist(T entity) {
        testEntityManager.persist(entity)
    }
}
