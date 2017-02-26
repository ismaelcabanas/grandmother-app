package cabanas.garcia.ismael.grandmother.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XI317311 on 20/02/2017.
 */
public final class RegExpUtil {
    private RegExpUtil() {
    }

    public static String replacePathVariable(String input, String regExp, String value){
        return input.replace("{" + regExp + "}", value);
    }
}
