package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

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

    BigDecimal balance = BigDecimal.ZERO
    
    String accountNumber

    @Embedded
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
            dateOfMovement: deposit.getDate(), person: deposit.getPerson(), description: deposit.description)
        addTransaction(depositMovement)
    }

    BigDecimal balance(){
        balance
    }

    def charge(Payment charge){
        balance = balance.subtract(charge.getAmount())
        PaymentTransaction chargeMovement = new PaymentTransaction(amount: charge.getAmount(),
            chargeType: charge.getType(), dateOfMovement: charge.getDate())
        addTransaction(chargeMovement)
    }


    String accountNumber() {
        accountNumber
    }

    private addTransaction(Transaction transaction){
        transaction.account = this
        transactions.add(transaction)
    }
}