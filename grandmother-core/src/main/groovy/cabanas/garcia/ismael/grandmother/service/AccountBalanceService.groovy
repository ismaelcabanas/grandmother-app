package cabanas.garcia.ismael.grandmother.service

interface AccountBalanceService {

    BigDecimal balance(long accountId, int year, int month)
}