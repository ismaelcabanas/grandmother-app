package cabanas.garcia.ismael.grandmother.model

/**
 * Created by XI317311 on 05/12/2016.
 */
interface Account {

    def deposit(BigDecimal amount, Person person, Date dateOfDeposit)

    BigDecimal balance()

    def debit(BigDecimal amount, Category category, ChargeType chargeType, Date dateOfCharge)
}