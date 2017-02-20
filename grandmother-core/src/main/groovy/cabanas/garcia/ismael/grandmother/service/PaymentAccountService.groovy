package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Transactions

/**
 * Created by XI317311 on 10/01/2017.
 */
interface PaymentAccountService {

    Transactions getPaymentTransactionsByYearAndMonth(Long accountId, int year, int month)

    Transactions getPaymentTransactionsByYear(Long accountId, int year)
}