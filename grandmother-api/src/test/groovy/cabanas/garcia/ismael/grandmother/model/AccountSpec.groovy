package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.AccountImpl
import cabanas.garcia.ismael.grandmother.model.impl.CategoryImpl
import cabanas.garcia.ismael.grandmother.model.impl.ChargeImpl
import cabanas.garcia.ismael.grandmother.model.impl.PersonImpl
import cabanas.garcia.ismael.grandmother.service.DebitMovementService
import cabanas.garcia.ismael.grandmother.service.DepositMovementService
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant


/**
 * Created by XI317311 on 05/12/2016.
 */
class AccountSpec extends Specification{

    @Unroll
    def "when #person.name deposits #givenAmount€ on an account with #currentBalance€ at #dateOfDeposit then the account's balance is #expectedBalance€"(){
        given: "mocks of deposit and debit movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with the mocks services and a current balance"
             Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "the person does the deposit"
            account.deposit(givenAmount, person, dateOfDeposit)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenAmount | expectedBalance | person                         | dateOfDeposit
        10.000         | 30.000      | 40.000          | new PersonImpl(name: "Ismael") | now()
        0              | 20.000      | 20.000          | new PersonImpl(name: "Ismael") | now()
    }

    @Unroll
    def "when #person.name deposits #givenAmount€ on an account with balance #currentBalance at #dateOfDeposit then the movement is registered in the system"(){
        DepositMovement depositMovement
        given: "mocks of deposit and debit movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with the mocks services and a current balance"
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "the person does the deposit"
            account.deposit(givenAmount, person, dateOfDeposit)
        then:
            1 * mockDepositMovementService.add(_) >> {arguments -> depositMovement = arguments[0]}
            0 * mockDebitMovementService.add(_)
        and:
            depositMovement.getAmount() == givenAmount
            depositMovement.getDateOfMovement() == dateOfDeposit
            depositMovement.getPerson() == person
        where:
        currentBalance | givenAmount | expectedBalance | person                         | dateOfDeposit
        10.000         | 30.000      | 40.000          | new PersonImpl(name: "Ismael") | now()

    }

    @Unroll
    def "when there is a charge of #charge.name of #givenAmount€ at #dateOfCharged on an account with balance #currentBalance€ then the account's balance is #expectedBalance" (){
        given: "mocks of deposit and debit movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with a determinaded balnce and the mocks services"
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "debit a charge on account"
            account.debit(givenAmountCharged, charge, dateOfDeposit)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenAmountCharged | expectedBalance | charge                       | dateOfDeposit
        10.000         | 30.000             | -20.000         | new ChargeImpl(name: "Agua") | now()
        30.000         | 20.000             | 10.000          | new ChargeImpl(name: "Agua") | now()
    }

    def "when there is a charge on an account then a movement is registered in the system" (){
        ChargeMovement chargedMovement

        given: "mocks of deposit and debit movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with a determinaded balance and the mocks services"
            BigDecimal currentBalance = new BigDecimal(20.000)
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        and: "a charge of a amount given"
            Charge charge = new ChargeImpl(name: "Agua")
            BigDecimal amountCharged = new BigDecimal(10.000)
            Date dateOfCharge = now()
        when: "debit a charge on account"
            account.debit(amountCharged, charge, dateOfCharge)
        then:
            1 * mockDebitMovementService.add(_) >> {arguments -> chargedMovement = arguments[0]}
            0 * mockDepositMovementService.add(_)
        and:
            chargedMovement.getAmount() == amountCharged.negate()
            chargedMovement.getCharge() == charge
            chargedMovement.getDateOfMovement() == dateOfCharge
    }

    private Date now() {
        Date.from(Instant.now())
    }
}
