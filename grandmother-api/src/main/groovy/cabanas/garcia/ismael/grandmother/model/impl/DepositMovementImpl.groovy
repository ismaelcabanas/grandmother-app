package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.DepositMovement
import cabanas.garcia.ismael.grandmother.model.Person
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class DepositMovementImpl extends MovementImpl implements DepositMovement{
    Person person

    @Override
    Person getGetPerson() {
        return person
    }
}
