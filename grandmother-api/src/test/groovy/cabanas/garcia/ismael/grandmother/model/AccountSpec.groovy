package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.AccountImpl
import cabanas.garcia.ismael.grandmother.model.impl.ChargeImpl
import cabanas.garcia.ismael.grandmother.model.impl.ChargeTypeImpl
import cabanas.garcia.ismael.grandmother.model.impl.DepositImpl
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
    def "when #givenDeposit.person.name deposits #givenDeposit.amount€ on an account with #currentBalance€ at #givenDeposit.date then the account's balance is #expectedBalance€"(){
        given: "mocks of deposit and chargeType movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with the mocks services and a current balance"
             Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "the person does the deposit"
            account.deposit(givenDeposit)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenDeposit                                                      | expectedBalance
        10.000         | new DepositImpl(amount: 30.000, person: getIsmael(), date: now()) | 40.000
        0              | new DepositImpl(amount: 20.000, person: getIsmael(), date: now()) | 20.000
    }

    @Unroll
    def "when #person.name deposits #givenAmount€ on an account with balance #currentBalance at #dateOfDeposit then the movement is registered in the system"(){
        DepositMovement depositMovement
        given: "mocks of deposit and chargeType movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with the mocks services and a current balance"
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "the person does the deposit"
            account.deposit(givenDeposit)
        then:
            1 * mockDepositMovementService.add(_) >> {arguments -> depositMovement = arguments[0]}
            0 * mockDebitMovementService.add(_)
        and:
            depositMovement.getAmount() == givenDeposit.getAmount()
            depositMovement.getDateOfMovement() == givenDeposit.getDate()
            depositMovement.getPerson() == givenDeposit.getPerson()
        where:
        currentBalance | givenDeposit                                                      | expectedBalance
        10.000         | new DepositImpl(amount: 30.000, person: getIsmael(), date: now()) | 40.000
    }

    @Unroll
    def "when there is a charge of #charge.type.name of #charge.amount€ at #charge.date on an account with balance #currentBalance€ then the account's balance is #expectedBalance" (){
        given: "mocks of deposit and chargeType movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with a determinaded balnce and the mocks services"
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        when: "chargeType a chargeType on account"
            account.charge(givenCharge)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenCharge                                                               | expectedBalance
        40.000         | new ChargeImpl(amount: 30.000, type: getWaterCharge(), date: now()) | 10.000

    }

    def "when there is a charge on an account then a movement is registered in the system" (){
        ChargeMovement chargedMovement

        given: "mocks of deposit and chargeType movements services"
            DepositMovementService mockDepositMovementService = Mock(DepositMovementService)
            DebitMovementService mockDebitMovementService = Mock(DebitMovementService)
        and: "an account with a determinaded balance and the mocks services"
            BigDecimal currentBalance = new BigDecimal(20.000)
            Account account = new AccountImpl(balance: currentBalance,
                depositMovementService: mockDepositMovementService, debitMovementService: mockDebitMovementService)
        and: "a charge of a amount given"
            Charge givenCharge = new ChargeImpl(amount: 10.000, type: getWaterCharge(), date: now())
        when: "chargeType a chargeType on account"
            account.charge(givenCharge)
        then:
            1 * mockDebitMovementService.add(_) >> {arguments -> chargedMovement = arguments[0]}
            0 * mockDepositMovementService.add(_)
        and:
            chargedMovement.getAmount() == givenCharge.getAmount()
            chargedMovement.getChargeType() == givenCharge.type
            chargedMovement.getDateOfMovement() == givenCharge.date
    }

    private Date now() {
        Date.from(Instant.now())
    }

    private Person getIsmael() {
        new PersonImpl(name: "Ismael")
    }

    private ChargeType getWaterCharge() {
        new ChargeTypeImpl(name: "Agua")
    }
}
