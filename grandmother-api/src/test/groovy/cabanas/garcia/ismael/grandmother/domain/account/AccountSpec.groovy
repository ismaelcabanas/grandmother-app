package cabanas.garcia.ismael.grandmother.domain.account

import cabanas.garcia.ismael.grandmother.domain.person.Person
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

/**
 * Created by XI317311 on 05/12/2016.
 */
class AccountSpec extends Specification{

    @Unroll
    def "when #givenDeposit.person.name deposits #givenDeposit.amount€ on an account with #currentBalance€ at #givenDeposit.date then the account's balance is #expectedBalance€"(){
        given: "an account with a balance"
             Account account = new Account(balance: currentBalance)
        when: "the person does the deposit"
            account.deposit(givenDeposit)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenDeposit                                                  | expectedBalance
        10.000         | new Deposit(amount: 30.000, person: getIsmael(), date: now()) | 40.000
        0              | new Deposit(amount: 20.000, person: getIsmael(), date: now()) | 20.000
    }

    @Unroll
    def "when there is a charge of #givenCharge.type.name of #givenCharge.amount€ at #givenCharge.date on an account with balance #currentBalance€ then the account's balance is #expectedBalance" (){
        given: "an account with a determinaded balance"
            Account account = new Account(balance: currentBalance)
        when: "charge the water receipt on account"
            account.charge(givenCharge)
        then:
            account.balance() == expectedBalance
        where:
        currentBalance | givenCharge                                                     | expectedBalance
        40.000         | new Charge(amount: 30.000, type: getWaterCharge(), date: now()) | 10.000
        20.000         | new Charge(amount: 30.000, type: getWaterCharge(), date: now()) | -10.000
    }

    def "when there is debits and charges on an account then the account generates movements"(){
        given: "an account with a balance"
            Account account = new Account(balance: 5.000)
        and: "it does two debits and one deposit on account"
            Charge chargeOne = new Charge(amount: 30.000, type: getWaterCharge(), date: now())
            Charge chargeTwo = new Charge(amount: 10.000, type: getWaterCharge(), date: now())
            Deposit depositOne = new Deposit(amount: 30.000, person: getIsmael(), date: now())
            account.charge(chargeOne)
            account.charge(chargeTwo)
            account.deposit(depositOne)
        when: "gets the account movements"
            Collection<Movement> movements = account.movements()
        then:
            movements.size() == 3
    }

    def "open an account"(){
        given: "an account number"
            String accountNumber = "ES123123123"
        when: "open an account"
            Account account = Account.open(accountNumber)
        then:
            account.accountNumber() == accountNumber
            account.balance() == BigDecimal.ZERO
    }

    def "buid accounts"(){
        when:
            Account account = Account.builder().id("1").accountNumber("123123").balance(30.000).build()
        then:
            account.id == "1"
            account.balance == 30.000
            account.accountNumber == "123123"
    }

    private Date now() {
        Date.from(Instant.now())
    }

    private Person getIsmael() {
        new Person(name: "Ismael")
    }

    private ChargeType getWaterCharge() {
        new ChargeType(name: "Agua")
    }
}
