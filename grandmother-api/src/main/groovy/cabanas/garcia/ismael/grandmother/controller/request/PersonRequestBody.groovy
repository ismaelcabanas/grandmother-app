package cabanas.garcia.ismael.grandmother.controller.request

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

@ToString
@EqualsAndHashCode
@Builder
class PersonRequestBody {
    @NotEmpty
    String name
}
