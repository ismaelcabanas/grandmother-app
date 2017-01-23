package cabanas.garcia.ismael.grandmother.stubs.repository

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTransactionRepository
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil

import java.util.stream.Collectors

/**
 * Created by XI317311 on 17/01/2017.
 */
class PaymenTransactionRepositoryStub implements PaymentTransactionRepository{

    Account account

    private Comparator<DepositTransaction> byDateOfTransactionAscendingOrder = { Transaction t1, Transaction t2 ->
        t2.dateOfMovement.compareTo(t1.dateOfMovement)
    }

    Object save(Object s) {
        return null
    }

    @Override
    def <S extends PaymentTransaction> Iterable<S> save(Iterable<S> iterable) {
        return null
    }

    @Override
    PaymentTransaction findOne(Long aLong) {
        return null
    }

    @Override
    boolean exists(Long aLong) {
        return false
    }

    @Override
    Iterable<PaymentTransaction> findAll() {
        return null
    }

    @Override
    Iterable<PaymentTransaction> findAll(Iterable<Long> iterable) {
        return null
    }

    @Override
    long count() {
        return 0
    }

    @Override
    void delete(Long aLong) {

    }

    @Override
    void delete(PaymentTransaction paymentTransaction) {

    }

    @Override
    void delete(Iterable<? extends PaymentTransaction> iterable) {

    }

    @Override
    void deleteAll() {

    }

    @Override
    Collection<PaymentTransaction> findByAccountIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(Long accountId, Date startDate, Date endDate) {
        Collection<PaymentTransaction> paymentTransactions =
                account.transactions.list
                .sort(false, byDateOfTransactionAscendingOrder)
                .stream()
                .filter({t -> t instanceof PaymentTransaction})
                .map({t -> (PaymentTransaction) t})
                .filter({PaymentTransaction dt -> dt.dateOfMovement >= startDate && dt.dateOfMovement <= endDate})
                .collect(Collectors.toList())
        return paymentTransactions
    }
}
