package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.utils.DateUtils

/**
 * Created by XI317311 on 25/01/2017.
 */
final class DepositUtil {

    public static String DEFAULT_DESCRIPTION = "Transferencia a su favor"

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
                .amount(AmountUtil.TWENTY_THOUSAND)
                .person(PersonUtil.getBea())
                .date(DateUtil.YESTERDAY)
                .description("Deposit Bea")
                .build()
    }

    static Deposit depositFrom(String personName, BigDecimal amount, String date) {
        Deposit.builder()
            .amount(amount)
            .person(PersonUtil.getPerson(personName))
            .date(DateUtils.parse(date))
            .description("deposit")
            .build()
    }
}

