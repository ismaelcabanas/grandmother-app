package cabanas.garcia.ismael.grandmother.domain.account

import cabanas.garcia.ismael.grandmother.domain.person.Person
import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
class DepositSpec extends Specification{

    def "buid deposits"(){
        when:
            Deposit deposit = Deposit.builder().amount(10).person(new Person(id: "1", name: "ismael"))
                .date(new Date()).build()
        then:
            deposit.amount == 10
            deposit.person == new Person(id: "1", name: "ismael")
            deposit.date != null
    }

}
