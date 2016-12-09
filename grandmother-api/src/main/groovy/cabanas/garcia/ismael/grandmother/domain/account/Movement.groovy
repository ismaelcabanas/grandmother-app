package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.ToString

import javax.persistence.*

/**
 * Created by XI317311 on 05/12/2016.
 */
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "movement_type")
abstract class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id

    @Column(nullable = false)
    BigDecimal amount

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateOfMovement

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Account account

}