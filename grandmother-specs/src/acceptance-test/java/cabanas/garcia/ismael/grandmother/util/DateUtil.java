package cabanas.garcia.ismael.grandmother.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XI317311 on 21/02/2017.
 */
public final class DateUtil {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean compareDates(String date1, String date2){
        boolean result = false;

        try {
            Date mDate1 = DATE_FORMAT.parse(date1);
            Date mDate2 = DATE_FORMAT.parse(date2);
            result = mDate1.equals(mDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return result;
    }
}
