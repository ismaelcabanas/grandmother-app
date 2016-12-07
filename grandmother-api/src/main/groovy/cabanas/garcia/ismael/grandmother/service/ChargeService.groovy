package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Charge

/**
 * Created by XI317311 on 05/12/2016.
 */
interface ChargeService {

    def create(Charge chargeType)
}