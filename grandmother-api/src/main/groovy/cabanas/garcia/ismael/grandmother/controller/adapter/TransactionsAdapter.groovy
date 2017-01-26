package cabanas.garcia.ismael.grandmother.controller.adapter

import cabanas.garcia.ismael.grandmother.controller.adapter.PersonAdapter
import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.DepositsResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentsResponse
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.utils.DateUtils

/**
 * Created by XI317311 on 05/01/2017.
 */
final class TransactionsAdapter {
    private TransactionsAdapter(){}

    static DepositsResponse mapDepositTransactionsEntityToResponse(Transactions transactions) {
        Collection<DepositResponse> depositTransactions = new ArrayList<>()

        transactions.list.each {Transaction transaction -> depositTransactions.add(mapDepositTransactionEntityToResponse(transaction))}
        
        return DepositsResponse.builder()
                .total(transactions.total)
                .deposits(depositTransactions)
                .build()
    }

    static DepositResponse mapDepositTransactionEntityToResponse(Transaction transaction) {
        DepositTransaction depositTransaction = (DepositTransaction) transaction
        DepositResponse.builder()
            .amount(transaction.amount)
            .date(DateUtils.format(transaction.dateOfMovement))
            .description(transaction.description)
            .person(PersonAdapter.mapPersonEntityToResponse(depositTransaction.person))
            .build()
    }

    static PaymentsResponse mapPaymentTransactionsEntityToResponse(Transactions transactions) {
        Collection<PaymentResponse> paymentTransactions = new ArrayList<>()

        transactions.list.each {Transaction transaction -> paymentTransactions.add(mapPaymentTransactionEntityToResponse(transaction))}

        return PaymentsResponse.builder()
                .total(transactions.total)
                .payments(paymentTransactions)
                .build()
    }

    static PaymentResponse mapPaymentTransactionEntityToResponse(Transaction transaction) {
        PaymentTransaction paymentTransaction = (PaymentTransaction) transaction
        PaymentResponse.builder()
                .amount(transaction.amount)
                .date(DateUtils.format(transaction.dateOfMovement))
                .paymentType(PaymentTypeAdapter.mapEntityToResponse(paymentTransaction.chargeType))
                .build()
    }
}
