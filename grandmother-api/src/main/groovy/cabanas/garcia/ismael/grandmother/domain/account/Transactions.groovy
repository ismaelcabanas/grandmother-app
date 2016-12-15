package cabanas.garcia.ismael.grandmother.domain.account

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 15/12/2016.
 */
@ToString
@EqualsAndHashCode
@Builder
class Transactions {

    Collection<Transaction> transactions

    Transactions(){
        transactions = new ArrayList<Transaction>()
    }

    def add(Transaction movement) {
        transactions.add(movement)
    }

    int size() {
        transactions.size()
    }
}
