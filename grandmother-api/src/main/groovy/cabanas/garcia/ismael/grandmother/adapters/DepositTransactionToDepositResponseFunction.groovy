package cabanas.garcia.ismael.grandmother.adapters

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.PersonResponse
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.utils.DateUtils

import java.util.function.Function

/**
 * Created by XI317311 on 22/12/2016.
 */
class DepositTransactionToDepositResponseFunction implements Function<DepositTransaction, DepositResponse>{

    @Override
    DepositResponse apply(DepositTransaction depositTransaction) {
        def builder = DepositResponse.builder()
            .amount(depositTransaction.amount)
            .date(DateUtils.format(depositTransaction.dateOfMovement))
            .description(depositTransaction.description)

        if(depositTransaction.person != null)
            builder.person(PersonResponse.builder().id(depositTransaction.person.id).name(depositTransaction.person.name).build())

        return builder.build()
    }
}
