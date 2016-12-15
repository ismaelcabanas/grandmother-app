package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.FetchType
import javax.persistence.OneToMany

/**
 * Created by XI317311 on 15/12/2016.
 */
@ToString
@EqualsAndHashCode
@Builder
@Embeddable
class Transactions {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    Collection<Transaction> transactions

    Transactions(){
        transactions = new ArrayList<Transaction>()
    }

    def add(Transaction movement) {
        transactions.add(movement)
    }

    int count() {
        transactions.size()
    }
}
