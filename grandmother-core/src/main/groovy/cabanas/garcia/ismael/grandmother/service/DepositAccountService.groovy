package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Transactions

/**
 * Created by XI317311 on 05/01/2017.
 */
interface DepositAccountService {

    Transactions getDepositTransactions(Long accountId)

    Transactions getDepositTransactionsByPersonId(Long accountId, Long personId)

    Transactions getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year)
}