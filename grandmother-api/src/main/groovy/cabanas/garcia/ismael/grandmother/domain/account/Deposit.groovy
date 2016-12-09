package cabanas.garcia.ismael.grandmother.domain.account

import cabanas.garcia.ismael.grandmother.domain.person.Person
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class Deposit {

    BigDecimal amount
    Person person
    Date date

}