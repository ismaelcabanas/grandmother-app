package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.service.AccountService

import java.util.stream.Collectors

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountServiceThatGetAnAccountStub extends AccountServiceStub implements AccountService {

    Account account

    AccountServiceThatGetAnAccountStub(Account account) {
        this.account = account
    }

    @Override
    Account get(Long accountId) {
        return account
    }

    @Override
    Collection<DepositTransaction> getDepositTransactions(Long accountId) {
        Comparator<DepositTransaction> byDateOfTransactionAscendingOrder =
                {DepositTransaction transaction1, DepositTransaction transaction2 ->
                    transaction2.dateOfMovement.compareTo(transaction1.dateOfMovement)}
        return account.transactions.list.sort(false, byDateOfTransactionAscendingOrder)
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId) {
        Comparator<DepositTransaction> byDateOfTransactionAscendingOrder =
                {DepositTransaction transaction1, DepositTransaction transaction2 ->
                    transaction2.dateOfMovement.compareTo(transaction1.dateOfMovement)}
        account.transactions.list
                .sort(false, byDateOfTransactionAscendingOrder)
                .stream()
                .filter({t -> t instanceof DepositTransaction})
                .map({t -> (DepositTransaction) t})
                .filter({DepositTransaction dt -> dt.person.id.equals(personId)})
                .collect(Collectors.toList())
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByYear(Long accountId, int year) {
        Comparator<DepositTransaction> byDateOfTransactionAscendingOrder =
                {DepositTransaction transaction1, DepositTransaction transaction2 ->
                    transaction2.dateOfMovement.compareTo(transaction1.dateOfMovement)}
        account.transactions.list
                .sort(false, byDateOfTransactionAscendingOrder)
                .stream()
                .filter({t -> t instanceof DepositTransaction})
                .map({t -> (DepositTransaction) t})
                .filter({DepositTransaction dt -> dt.dateOfMovement[Calendar.YEAR] == year})
                .collect(Collectors.toList())
    }
}
