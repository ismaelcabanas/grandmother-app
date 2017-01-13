package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.*
import cabanas.garcia.ismael.grandmother.service.PaymentAccountService
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil

/**
 * Created by XI317311 on 10/01/2017.
 */
class PaymentAccountServiceWithPaymentsInAccountStub implements PaymentAccountService {
    Account account

    private Comparator<DepositTransaction> byDateOfTransactionAscendingOrder = { Transaction transaction1, Transaction transaction2 ->
        transaction2.dateOfMovement.compareTo(transaction1.dateOfMovement)
    }

    def PaymentAccountServiceWithPaymentsInAccountStub(Account account) {
        this.account = account
    }

    @Override
    Transactions getPaymentTransactionsByYearAndMonth(Long accountId, int year, int month) {
        Collection<DepositTransaction> paymentTransactions =
                account.transactions.list.sort(false, this.byDateOfTransactionAscendingOrder)

        Collection<DepositTransaction> paymentTransactionsByYearAndMonth =
                paymentTransactions.findAll { PaymentTransaction dt ->
                    (DateUtil.yearOf(dt.dateOfMovement) == year
                        && DateUtil.monthOf(dt.dateOfMovement) == month-1)}

        Transactions transactions = new Transactions()

        paymentTransactionsByYearAndMonth.each {PaymentTransaction dt -> transactions.add(dt)}

        transactions
    }
}
