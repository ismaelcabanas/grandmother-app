package cabanas.garcia.ismael.grandmother.utils

import java.text.SimpleDateFormat

/**
 * Created by XI317311 on 23/12/2016.
 */
final class DateUtils {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    private DateUtils(){}

    static String format(Date date) {
        assert date != null
        DATE_FORMAT.format(date)
    }

    static Date lastDayOfYear(int year) {
        def calEndDate = Calendar.instance
        calEndDate.set(year: year, month: Calendar.DECEMBER, date: 31)
        calEndDate.time
    }

    static Date firstDateOfYear(int year) {
        def calStartDate = Calendar.instance
        calStartDate.set(year: year, month: Calendar.JANUARY, date: 1)
        calStartDate.time
    }

    static Date parse(String date){
        assert date != null
        DATE_FORMAT.parse(date)
    }
}
