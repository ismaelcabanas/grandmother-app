package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Deposit
import cabanas.garcia.ismael.grandmother.model.Person
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class DepositImpl implements Deposit{
    private BigDecimal amount
    private Person person
    private Date date

    @Override
    BigDecimal getAmount() {
        return amount
    }

    @Override
    Date getDate() {
        return date
    }

    @Override
    Person getPerson() {
        return person
    }
}
