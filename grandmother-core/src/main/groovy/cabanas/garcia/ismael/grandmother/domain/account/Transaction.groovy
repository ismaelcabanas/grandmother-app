package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * Created by XI317311 on 05/12/2016.
 */
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "movement_type")
@Builder
abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    BigDecimal amount

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateOfMovement

    @ManyToOne
    @JoinColumn(nullable = false)
    Account account

    @Column(nullable = false)
    String description

}