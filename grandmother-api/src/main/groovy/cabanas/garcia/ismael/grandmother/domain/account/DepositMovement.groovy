package cabanas.garcia.ismael.grandmother.domain.account

import cabanas.garcia.ismael.grandmother.domain.person.Person
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
@Entity
@DiscriminatorValue("DEPOSIT")
class DepositMovement extends Movement{
    @ManyToOne
    Person person

}
