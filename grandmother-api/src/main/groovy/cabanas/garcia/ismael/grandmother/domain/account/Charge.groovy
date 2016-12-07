package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class Charge {

    private BigDecimal amount
    private ChargeType type
    private Date date

    BigDecimal getAmount() {
        return amount
    }

    ChargeType getType() {
        return type
    }

    Date getDate() {
        return date
    }
}