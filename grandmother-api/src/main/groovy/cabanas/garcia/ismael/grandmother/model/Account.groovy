package cabanas.garcia.ismael.grandmother.model

/**
 * Created by XI317311 on 05/12/2016.
 */
interface Account {

    def deposit(Deposit deposit)

    BigDecimal balance()

    def charge(Charge charge)
}