package cabanas.garcia.ismael.grandmother.domain.account

import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
class ChargeSpec extends Specification{

    def "build charges"(){
        when:
            Charge charge = Charge.builder().amount(20).type(new ChargeType(name: "Agua")).build()
        then:
            charge.amount == 20
            charge.type == new ChargeType(name: "Agua")
    }
}
