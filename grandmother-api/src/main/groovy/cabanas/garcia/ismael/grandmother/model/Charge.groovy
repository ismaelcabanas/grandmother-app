package cabanas.garcia.ismael.grandmother.model

/**
 * Created by XI317311 on 05/12/2016.
 */
interface Charge {

    def create()

    BigDecimal getAmount()

    ChargeType getType()

    Date getDate()
}