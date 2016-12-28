package cabanas.garcia.ismael.grandmother.domain.account

import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
class PaymentSpec extends Specification{

    def "build charges"(){
        when:
            Payment payment = Payment.builder().amount(20).type(new PaymentType(name: "Agua")).build()
        then:
            payment.amount == 20
            payment.type == new PaymentType(name: "Agua")
    }
}
