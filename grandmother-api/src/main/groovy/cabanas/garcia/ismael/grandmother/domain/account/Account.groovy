package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.Min

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

    @Min(value = 0L)
    BigDecimal balance = BigDecimal.ZERO
    
    @NotEmpty
    String accountNumber

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    Collection<Transaction> movements = new ArrayList<Transaction>()

    Transactions transactions

    static Account open(String accountNumber, BigDecimal balance = BigDecimal.ZERO) {
        return new Account(balance: balance, accountNumber: accountNumber)
    }

    Account(){
        transactions = new Transactions()
    }

    def deposit(Deposit deposit){
        balance = balance.add(deposit.getAmount())
        DepositTransaction depositMovement = new DepositTransaction(amount: deposit.getAmount(),
            dateOfMovement: deposit.getDate(), person: deposit.getPerson())
        addTransaction(depositMovement)
    }

    BigDecimal balance(){
        balance
    }

    def charge(Charge charge){
        balance = balance.subtract(charge.getAmount())
        ChargeTransaction chargeMovement = new ChargeTransaction(amount: charge.getAmount(),
            chargeType: charge.getType(), dateOfMovement: charge.getDate())
        addTransaction(chargeMovement)
    }

    Collection<Transaction> movements() {
        movements
    }

    def movements(Transactions movements){

    }

    String accountNumber() {
        accountNumber
    }

    private addTransaction(Transaction movement){
        transactions.add(movement)
    }
}