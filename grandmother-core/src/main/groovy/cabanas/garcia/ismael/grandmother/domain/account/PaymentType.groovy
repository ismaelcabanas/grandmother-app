package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
@Entity
class PaymentType {

    @Column(nullable = false)
    @NotEmpty
    String name

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

}