package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.service.DepositAccountService
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.DateUtils

/**
 * Created by XI317311 on 05/01/2017.
 */
class DepositAccountServiceWithDepositsInAccountStub implements DepositAccountService{
    Account account

    private Comparator<DepositTransaction> byDateOfTransactionAscendingOrder = { DepositTransaction transaction1, DepositTransaction transaction2 ->
        transaction2.dateOfMovement.compareTo(transaction1.dateOfMovement)
    }

    @Override
    Transactions getDepositTransactions(Long accountId) {
        Collection<DepositTransaction> depositTransactions =
                account.transactions.list.sort(false, this.byDateOfTransactionAscendingOrder)

        Transactions transactions = new Transactions()

        depositTransactions.each {DepositTransaction dt -> transactions.add(dt)}

        transactions
    }

    @Override
    Transactions getDepositTransactionsByPersonId(Long accountId, Long personId) {
        Collection<DepositTransaction> depositTransactions =
                account.transactions.list.sort(false, this.byDateOfTransactionAscendingOrder)

        Collection<DepositTransaction> depositTransactionsByPerson = depositTransactions.findAll {DepositTransaction dt -> dt.person.id.equals(personId)}

        Transactions transactions = new Transactions()

        depositTransactionsByPerson.each {DepositTransaction dt -> transactions.add(dt)}

        transactions
    }

    @Override
    Transactions getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year) {
        Collection<DepositTransaction> depositTransactions =
                account.transactions.list.sort(false, this.byDateOfTransactionAscendingOrder)

        Collection<DepositTransaction> depositTransactionsByPersonAndYear =
                depositTransactions.findAll {DepositTransaction dt ->
                    (dt.person.id.equals(personId) &&
                            DateUtilTest.yearOf(dt.dateOfMovement) == year)}

        Transactions transactions = new Transactions()

        depositTransactionsByPersonAndYear.each {DepositTransaction dt -> transactions.add(dt)}

        transactions
    }
}
