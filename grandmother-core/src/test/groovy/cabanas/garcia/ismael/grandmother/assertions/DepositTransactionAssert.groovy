package cabanas.garcia.ismael.grandmother.assertions

import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import org.assertj.core.api.AbstractAssert

/**
 * Created by XI317311 on 26/01/2017.
 */
class DepositTransactionAssert extends AbstractAssert<DepositTransactionAssert, DepositTransaction>{

    List<DepositTransaction> depositTransactions

    DepositTransactionAssert(List<DepositTransaction> depositTransactions) {
        this(new DepositTransaction())
        this.depositTransactions = depositTransactions
    }

    DepositTransactionAssert(DepositTransaction actual) {
        super(actual, DepositTransactionAssert.class)
    }

    static DepositTransactionAssert assertThat(Collection<DepositTransaction> depositTransactions) {
        List<DepositTransaction> depositTransactionInList = new ArrayList<>()
        depositTransactionInList.addAll(depositTransactions)
        return new DepositTransactionAssert(depositTransactionInList)
    }

    DepositTransactionAssert containsInOrder(Deposit... deposits) {
        if(deposits == null)
            failWithMessage("Deposits can not be null")

        depositTransactions.eachWithIndex { DepositTransaction depositTransaction, int i ->
            Deposit deposit = deposits[i]
            if(!depositTransaction.getAmount().toDouble().equals(deposit.getAmount().toDouble())
                && !depositTransaction.getDescription().equals(deposit.getDescription())
                && !depositTransaction.getDateOfMovement().equals(deposit.getDate())
                && !depositTransaction.getPerson().getName().equals(deposit.getPerson().getName())){
                    failWithMessage("Expected deposit transaction <%s> but was <%s>", depositTransaction, deposit)
            }
        }
        return this
    }
}
