package cabanas.garcia.ismael.grandmother.domain.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
@Entity
class Person {

    @Id
    @GeneratedValue
    String id
    String name



    def changeName(String newName) {
        name = newName
    }
}