package cabanas.garcia.ismael.grandmother.domain.person

import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
class PersonSpec extends Specification {

    def "build person"(){
        when:
            Person person = Person.builder().id(1).name("Ismael").build()
        then:
            person.id == 1
            person.name == "Ismael"
    }
}
