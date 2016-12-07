package cabanas.garcia.ismael.grandmother.model.impl

import cabanas.garcia.ismael.grandmother.model.ChargeType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class ChargeTypeImpl implements ChargeType {

    private String name

    @Override
    String getName() {
        return name
    }
}
