package cabanas.garcia.ismael.grandmother.functions

import cabanas.garcia.ismael.grandmother.controller.response.DepositResponse
import cabanas.garcia.ismael.grandmother.controller.response.PersonResponse
import cabanas.garcia.ismael.grandmother.domain.account.Deposit

import java.util.function.Function

/**
 * Created by XI317311 on 22/12/2016.
 */
class DepositToDepositResponseFunction implements Function<Deposit, DepositResponse>{

    @Override
    DepositResponse apply(Deposit deposit) {
        return DepositResponse.builder()
            .amount(deposit.amount)
            .date(deposit.date)
            .person(PersonResponse.builder().id(deposit.person.id).name(deposit.person.name).build())
            .description(deposit.description)
            .build()
    }
}
