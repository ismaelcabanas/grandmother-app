package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPaymentAccountService
import cabanas.garcia.ismael.grandmother.stubs.repository.PaymenTransactionRepositoryStub
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil
import spock.lang.Specification

import java.util.concurrent.ArrayBlockingQueue

import static cabanas.garcia.ismael.grandmother.utils.DateUtils.*
import static cabanas.garcia.ismael.grandmother.utils.test.AccountUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.*

/**
 * Created by XI317311 on 16/01/2017.
 */
class RepositoryPaymentAccountServiceSpec extends Specification{

    def "should return payment transactions in ascending order by date for given account on a year and month"(){
        given: "a given account with transactions"
            Account account = Account.builder().id(1).balance(BigDecimal.ZERO).transactions(new Transactions())build()
            def aguaPayment = new Payment(amount: TEN_THOUSAND, type: getAguaPayment(), date: parse("2016-01-01 00:00:00.0"))
            def gasPayment = new Payment(amount: TWENTY_THOUSAND, type: getGasPayment(), date: parse("2016-01-20 00:00:00.0"))
            def endesaPayment = new Payment(amount: TEN_THOUSAND, type: getEndesaPayment(), date: parse("2015-02-01 00:00:00.0"))
            def ismaelDeposit = new Deposit(amount: TEN_THOUSAND, person: PersonUtil.ismael, date: parse("2016-01-01 00:00:00.0"))
            account.charge(aguaPayment)
            account.charge(gasPayment)
            account.charge(endesaPayment)
            account.deposit(ismaelDeposit)
        and:
            PaymentTransactionRepository paymentTransactionRepository =
                    new PaymenTransactionRepositoryStub(account:  account)
            PaymentAccountService paymentAccountService =
                    new RepositoryPaymentAccountService(paymentTransactionRepository: paymentTransactionRepository)
        and: "year and month for consulting payment transactions"
            int year = 2016
            int month = 1
        when:
            Transactions paymentTransactions =
                    paymentAccountService.getPaymentTransactionsByYearAndMonth(account.id, year, month)
        then:
            paymentTransactions.areEmpty() == false
            paymentTransactions.total == THIRTY_THOUSAND
            paymentTransactions.count() == 2
            paymentTransactionsAre(paymentTransactions, aguaPayment, gasPayment)
    }

    void paymentTransactionsAre(Transactions transactions, Payment... payments) {
        transactions.list.each {PaymentTransaction pt ->
            assert payments.contains(new Payment(amount: pt.amount, type: pt.chargeType, date: pt.dateOfMovement))
        }
    }
}
