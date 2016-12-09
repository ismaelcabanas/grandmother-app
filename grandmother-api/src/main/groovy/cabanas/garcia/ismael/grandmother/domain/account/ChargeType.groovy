package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by XI317311 on 07/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
@Entity
class ChargeType {

    @Column(nullable = false)
    String name

    @Id
    @GeneratedValue
    String id

}