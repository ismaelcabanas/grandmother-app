package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.service.DebitMovementService
import cabanas.garcia.ismael.grandmother.service.DepositMovementService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class AccountImpl implements Account{
    private BigDecimal balance
    private DepositMovementService depositMovementService
    private DebitMovementService debitMovementService

    @Override
    def deposit(BigDecimal amount, Person person, Date dateOfDeposit) {
        DepositMovement movement = new DepositMovementImpl(amount: amount, person: person, dateOfMovement: dateOfDeposit)
        depositMovementService.add(movement)
        balance = balance.add(amount)
    }

    @Override
    def debit(BigDecimal amount, Category category, ChargeType chargeType, Date dateOfCharge) {
        BigDecimal debitAmount = amount.negate()
        ChargeMovement movement = new ChargeMovementImpl(amount: debitAmount, category: category, chargeType: chargeType,
            dateOfMovement: dateOfCharge)
        debitMovementService.add(movement)
        balance = balance.subtract(amount)
    }

    BigDecimal balance(){
        balance
    }
}
