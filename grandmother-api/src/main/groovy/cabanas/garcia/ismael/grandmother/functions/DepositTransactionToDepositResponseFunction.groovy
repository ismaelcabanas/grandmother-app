package cabanas.garcia.ismael.grandmother.functions

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.PersonResponse
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction

import java.util.function.Function

/**
 * Created by XI317311 on 22/12/2016.
 */
class DepositTransactionToDepositResponseFunction implements Function<DepositTransaction, DepositResponse>{

    @Override
    DepositResponse apply(DepositTransaction depositTransaction) {
        def builder = DepositResponse.builder()
            .amount(depositTransaction.amount)
            .date(depositTransaction.dateOfMovement)
            .description(depositTransaction.description)

        if(depositTransaction.person != null)
            builder.person(PersonResponse.builder().id(depositTransaction.person.id).name(depositTransaction.person.name).build())

        return builder.build()
    }
}
