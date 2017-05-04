package cabanas.garcia.ismael.grandmother.stubs.repository

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.domain.account.repository.TransactionRepository

class DefaultTransactionRepositoryStub implements TransactionRepository{
    Account account
    Collection<Transaction> transactions

    DefaultTransactionRepositoryStub(Account account, Collection<Transaction> transactions) {
        this.account = account
        this.transactions = transactions
    }

    @Override
    Collection<Transaction> findByAccountIdAndDateOfMovementLessThan(Long accountId, Date endDate) {
        if(account.id.equals(accountId))
            return transactions.stream().filter {t -> t.dateOfMovement <= endDate}.collect()
        return new ArrayList<Transaction>()
    }
}
