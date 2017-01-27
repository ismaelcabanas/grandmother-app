package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPaymentAccountService
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.DateUtils.parse
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getAguaPersistedPayment
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getGasPersistedPayment

/**
 * Created by XI317311 on 16/01/2017.
 */
class RepositoryPaymentAccountServiceSpec extends Specification{

    def "should return payment transactions in ascending order by date for given account on a year and month"(){
        given: "a given account with transactions"
            Account account = Account.builder().id(1).balance(BigDecimal.ZERO).transactions(new Transactions())build()
            Collection<PaymentTransaction> paymentsTransactions = new ArrayList<>()
            PaymentTransaction aguaPayment = new PaymentTransaction(amount: AmountUtil.TEN_THOUSAND, chargeType: aguaPersistedPayment(), dateOfMovement: parse("2016-01-01 00:00:00.0"))
            PaymentTransaction gasPayment = new PaymentTransaction(amount: AmountUtil.TWENTY_THOUSAND, chargeType: gasPersistedPayment(), dateOfMovement: parse("2016-01-20 00:00:00.0"))
            paymentsTransactions.add(aguaPayment)
            paymentsTransactions.add(gasPayment)
            /*def aguaPayment = new Payment(amount: TEN_THOUSAND, type: getAguaPersistedPayment(), date: parse("2016-01-01 00:00:00.0"))
            def gasPayment = new Payment(amount: TWENTY_THOUSAND, type: getGasPersistedPayment(), date: parse("2016-01-20 00:00:00.0"))
            def endesaPayment = new Payment(amount: TEN_THOUSAND, type: getEndesaPersistedPayment(), date: parse("2015-02-01 00:00:00.0"))
            def ismaelDeposit = new Deposit(amount: TEN_THOUSAND, person: PersonUtil.ismael, date: parse("2016-01-01 00:00:00.0"))
            account.charge(aguaPayment)
            account.charge(gasPayment)
            account.charge(endesaPayment)
            account.deposit(ismaelDeposit)*/

        and:
            PaymentTransactionRepository paymentTransactionRepository = Mock(PaymentTransactionRepository.class)
            PaymentAccountService paymentAccountService =
                    new RepositoryPaymentAccountService(paymentTransactionRepository: paymentTransactionRepository)
        and: "year and month for consulting payment transactions"
            int year = 2016
            int month = 1
        when:
            Transactions paymentTransactions =
                    paymentAccountService.getPaymentTransactionsByYearAndMonth(account.id, year, month)
        then:
            1 * paymentTransactionRepository.findByAccountIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(account.id, _, _) >> paymentsTransactions
            paymentTransactions.areEmpty() == false
            paymentTransactions.total == AmountUtil.THIRTY_THOUSAND
            paymentTransactions.count() == 2
            paymentTransactionsAre(paymentTransactions, aguaPayment, gasPayment)
    }

    void paymentTransactionsAre(Transactions transactions, PaymentTransaction... payments) {
        transactions.list.each {PaymentTransaction pt ->
            assert payments.contains(pt)
        }
    }
}
