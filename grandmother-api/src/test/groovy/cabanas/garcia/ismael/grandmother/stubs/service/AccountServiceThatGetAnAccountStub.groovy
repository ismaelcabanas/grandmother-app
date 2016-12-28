package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.service.AccountService

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountServiceThatGetAnAccountStub extends AccountServiceStub implements AccountService {

    Account account

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
}
