package cabanas.garcia.ismael.grandmother.utils

import java.text.SimpleDateFormat

/**
 * Created by XI317311 on 23/12/2016.
 */
class DateUtils {
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    static String format(Date date) {
        assert date != null
        DATE_FORMAT.format(date)
    }
}
