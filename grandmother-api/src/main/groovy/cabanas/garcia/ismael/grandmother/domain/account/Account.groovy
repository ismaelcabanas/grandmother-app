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
class Account {
    private BigDecimal balance
    List<Movement> movements

    Account(){
        movements = new ArrayList<Movement>()
    }

    def deposit(Deposit deposit){
        balance = balance.add(deposit.getAmount())
        DepositMovement depositMovement = new DepositMovement(amount: deposit.getAmount(),
            dateOfMovement: deposit.getDate(), person: deposit.getPerson())
        movements.add(depositMovement)
    }

    BigDecimal balance(){
        balance
    }

    def charge(Charge charge){
        balance = balance.subtract(charge.getAmount())
        ChargeMovement chargeMovement = new ChargeMovement(amount: charge.getAmount(),
            chargeType: charge.getType(), dateOfMovement: charge.getDate())
        movements.add(chargeMovement)
    }

    List<Movement> movements() {
        movements
    }
}