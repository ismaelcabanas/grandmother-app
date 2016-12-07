package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.Account
import cabanas.garcia.ismael.grandmother.model.ChargeMovement
import cabanas.garcia.ismael.grandmother.model.Charge
import cabanas.garcia.ismael.grandmother.model.Deposit
import cabanas.garcia.ismael.grandmother.model.DepositMovement
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
    def deposit(Deposit deposit) {
        DepositMovement movement = new DepositMovementImpl(amount: deposit.getAmount(),
                person: deposit.getPerson(), dateOfMovement: deposit.getDate())
        depositMovementService.add(movement)
        balance = balance.add(deposit.getAmount())
    }

    @Override
    BigDecimal balance(){
        balance
    }

    @Override
    def charge(Charge charge) {
        BigDecimal debitAmount = charge.getAmount()
        ChargeMovement movement = new ChargeMovementImpl(amount: debitAmount, chargeType: charge.getType(),
                dateOfMovement: charge.getDate())
        debitMovementService.add(movement)
        balance = balance.subtract(debitAmount)
    }
}
