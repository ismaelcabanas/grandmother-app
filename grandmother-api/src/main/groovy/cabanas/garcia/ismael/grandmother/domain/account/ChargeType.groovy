package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class ChargeType {
    private String name

    String getName(){
        name
    }
}