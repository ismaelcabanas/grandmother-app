package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account

/**
 * Created by XI317311 on 09/12/2016.
 */
interface AccountService {

    Account open(String accountNumber)

    Account deposit(String accountId, String personId, BigDecimal amount, Date date)
}