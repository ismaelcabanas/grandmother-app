package cabanas.garcia.ismael.grandmother.controller.response

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 03/01/2017.
 */
@ToString
@Builder
@EqualsAndHashCode
class PaymentTypesResponse {
    Collection<PaymentTypeResponse> paymentTypes
}
