package cabanas.garcia.ismael.grandmother.utils
/**
 * Utility test class for managing dates.
 *
 * Created by XI317311 on 20/12/2016.
 */
class DateUtilTest {
    static Date TODAY = new Date()
    static Date YESTERDAY = new Date().previous()

    static int yearOf(Date date) {
        date[Calendar.YEAR]
    }

    static Date lastYearFrom(Date date){
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.YEAR, -1)
        return calendar.getTime()
    }
}
