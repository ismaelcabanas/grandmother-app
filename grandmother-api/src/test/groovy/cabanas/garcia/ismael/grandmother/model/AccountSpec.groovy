package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.Account
import cabanas.garcia.ismael.grandmother.model.AccountImpl
import cabanas.garcia.ismael.grandmother.model.CategoryImpl
import cabanas.garcia.ismael.grandmother.model.ChargeMovement
import cabanas.garcia.ismael.grandmother.model.ChargeType
import cabanas.garcia.ismael.grandmother.model.ChargeTypeImpl
import cabanas.garcia.ismael.grandmother.model.DepositMovement
import cabanas.garcia.ismael.grandmother.model.Person
import cabanas.garcia.ismael.grandmother.model.PersonImpl
import cabanas.garcia.ismael.grandmother.service.DebitMovementService
import cabanas.garcia.ismael.grandmother.service.DepositMovementService
import cabanas.garcia.ismael.grandmother.model.Category
import spock.lang.Specification

import java.time.Instant


/**
 * Created by XI317311 on 05/12/2016.
 */
class AccountSpec extends Specification{

    def "Deposit on an account realized for a person in a determinaded moment produces the account's balance is the sum of current balance plus money deposited for that person"(){
        given:
        BigDecimal currentBalance = 10.000
        BigDecimal amount = 30.000
        Date currentDate = Date.from(Instant.now())
        DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
        DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        Person person = new PersonImpl(name: "Ismael")

        when:
        account.deposit(amount, person, currentDate)

        then:
        account.balance() == currentBalance + amount
    }

    def "Deposit on an account realized for a person in a determinaded moment produces that the movement is registered in the system"(){
        DepositMovement depositMovement
        given:
        BigDecimal currentBalance = 10.000
        BigDecimal amount = 30.000
        DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
        DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        Person person = new PersonImpl(name: "Ismael")
        Date dateOfDeposit = Date.from(Instant.now())

        when:
        account.deposit(amount, person, dateOfDeposit)

        then:
        1 * mockDepositMovementService.add(_) >> {arguments -> depositMovement = arguments[0]}
        0 * mockDebitMovementService.add(_)

        and:
            depositMovement.getAmount() == amount
            depositMovement.getDateOfMovement() == dateOfDeposit
            depositMovement.getPerson() == person
    }

    def "Debit on account in a moment the account's balance is the current balance less the amount charged" (){
        given:
        BigDecimal currentBalance = 10.000
        BigDecimal amountCharged = 30.000
        DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
        DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        Person person = new PersonImpl(name: "Ismael")
        Date dateOfCharged = Date.from(Instant.now())
        Category category = new CategoryImpl(name: "Hogar")
        ChargeType chargeType = new ChargeTypeImpl(name: "Agua")

        when:
        account.debit(amountCharged, category, chargeType, dateOfCharged)

        then:
        account.balance() == currentBalance - amountCharged

    }

    def "Debit on account in a moment produces that the movement is registered in the system" (){
        ChargeMovement chargedMovement

        given:
        BigDecimal currentBalance = 10.000
        BigDecimal amountCharged = 30.000
        DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
        DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        Person person = new PersonImpl(name: "Ismael")
        Date dateOfCharged = Date.from(Instant.now())
        Category category = new CategoryImpl(name: "Hogar")
        ChargeType chargeType = new ChargeTypeImpl(name: "Agua")

        when:
        account.debit(amountCharged, category, chargeType, dateOfCharged)

        then:
        1 * mockDebitMovementService.add(_) >> {arguments -> chargedMovement = arguments[0]}
        0 * mockDepositMovementService.add(_)

        and:
        chargedMovement.getAmount() == amountCharged.negate()
        chargedMovement.getCategory() == category
        chargedMovement.getChargeType() == chargeType
        chargedMovement.getDateOfMovement() == dateOfCharged

    }
}
