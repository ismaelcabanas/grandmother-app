package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.Account
import org.springframework.data.repository.CrudRepository

/**
 * Created by XI317311 on 09/12/2016.
 */
interface AccountRepository extends CrudRepository<Account, String>{

}