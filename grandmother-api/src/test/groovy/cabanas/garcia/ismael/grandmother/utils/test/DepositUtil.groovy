package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.Deposit

/**
 * Created by XI317311 on 25/01/2017.
 */
final class DepositUtil {
    static Deposit depositFromIsmaelOf10000Today() {
        Deposit.builder()
                .amount(AmountUtil.TEN_THOUSAND)
                .person(PersonUtil.getIsmael())
                .date(DateUtil.TODAY)
                .description("Deposit Ismael")
                .build()
    }

    static Deposit depositFromBeaOf20000Yesterday() {
        Deposit.builder()
                .amount(AmountUtil.TEN_THOUSAND)
                .person(PersonUtil.getBea())
                .date(DateUtil.YESTERDAY)
                .description("Deposit Bea")
                .build()
    }
}

