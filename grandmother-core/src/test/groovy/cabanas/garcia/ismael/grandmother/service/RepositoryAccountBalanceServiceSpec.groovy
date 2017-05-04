package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.domain.account.repository.TransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryAccountBalanceService
import cabanas.garcia.ismael.grandmother.stubs.repository.DefaultTransactionRepositoryStub
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import spock.lang.Specification
import spock.lang.Unroll

import static cabanas.garcia.ismael.grandmother.utils.test.AccountUtil.getDefaultAccountPersisted
import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.TEN_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.TWENTY_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.ZERO
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.ENDESA_PAYMENT
import static cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getPersistedIsmael

class RepositoryAccountBalanceServiceSpec extends Specification{

    @Unroll
    def "should return balance of an account until year #year and month #month"(){
        given: "a given account with transactions"
            Account account = getDefaultAccountPersisted()
            Collection<Transaction> transactionsInRepository = transactions.toList()
        and:
            TransactionRepository transactionRepository = new DefaultTransactionRepositoryStub(account, transactionsInRepository)
            AccountBalanceService sut =
                    new RepositoryAccountBalanceService(transactionRepository)
        when:
            BigDecimal actual = sut.balance(account.id, year, month)
        then:
            //1 * transactionRepository.balance(account.id, year, month)
            actual == balanceExpected
        where:
        year | month | transactions                                                                                                    | balanceExpected          | comments
        2010 | 1     | [deposit(TEN_THOUSAND, "2010-01-01"), deposit(TEN_THOUSAND, "2008-05-01"), payment(TEN_THOUSAND, "2010-01-25")] | TEN_THOUSAND             | "exist transactions(deposits and payments)"
        2006 | 1     | [deposit(TEN_THOUSAND, "2010-01-01"), deposit(TEN_THOUSAND, "2008-05-01"), payment(TEN_THOUSAND, "2010-01-25")] | ZERO                     | "not exist transactions to this year and month"
        2010 | 1     | [payment(TEN_THOUSAND, "2010-01-25"), payment(TEN_THOUSAND, "2009-04-20")]                                      | TWENTY_THOUSAND.negate() | "only payments"
        2010 | 1     | [deposit(TEN_THOUSAND, "2010-01-25"), deposit(TEN_THOUSAND, "2009-04-20")]                                      | TWENTY_THOUSAND          | "only deposits"
    }

    def payment(BigDecimal amount, String date) {
        new PaymentTransaction(account: getDefaultAccountPersisted(), amount: amount, dateOfMovement: DateUtils.parse(date + " 00:00:00.0"), chargeType: ENDESA_PAYMENT)
    }

    def deposit(BigDecimal amount, String date) {
        new DepositTransaction(account: getDefaultAccountPersisted(), amount: amount, dateOfMovement: DateUtils.parse(date + " 00:00:00.0"), person: persistedIsmael)
    }

}
