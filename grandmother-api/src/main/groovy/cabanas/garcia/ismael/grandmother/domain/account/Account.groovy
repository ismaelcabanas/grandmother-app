package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
@Entity
class Account {
    private static final BigDecimal ZERO_BALANCE = BigDecimal.ZERO

    @Id
    @GeneratedValue
    String id

    BigDecimal balance
    String accountNumber

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    Collection<Movement> movements = new ArrayList<Movement>()


    static Account open(String accountNumber) {
        return new Account(balance: ZERO_BALANCE, accountNumber: accountNumber)
    }


    def deposit(Deposit deposit){
        balance = balance.add(deposit.getAmount())
        DepositMovement depositMovement = new DepositMovement(amount: deposit.getAmount(),
            dateOfMovement: deposit.getDate(), person: deposit.getPerson())
        addMovement(depositMovement)
    }

    BigDecimal balance(){
        balance
    }

    def charge(Charge charge){
        balance = balance.subtract(charge.getAmount())
        ChargeMovement chargeMovement = new ChargeMovement(amount: charge.getAmount(),
            chargeType: charge.getType(), dateOfMovement: charge.getDate())
        addMovement(chargeMovement)
    }

    Collection<Movement> movements() {
        movements
    }

    String accountNumber() {
        accountNumber
    }

    private addMovement(Movement movement){
        movement.account = this
        movements.add(movement)
    }
}