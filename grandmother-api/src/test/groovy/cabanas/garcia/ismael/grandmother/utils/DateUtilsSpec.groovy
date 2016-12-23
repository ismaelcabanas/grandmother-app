package cabanas.garcia.ismael.grandmother.utils

import spock.lang.Specification

/**
 * Created by XI317311 on 23/12/2016.
 */
class DateUtilsSpec extends Specification{
    def "should throw exception when format a null date"(){
        when:
            DateUtils.format(null)
        then:
            thrown Error
    }

    def "should return string formatted date in format yyyy-MM-dd"(){
        given:
            def date = new GregorianCalendar(2014, Calendar.APRIL, 3, 1, 23, 45).time
            String expectedFormattedDay = "2014-04-03 01:23:45.0"
        when:
            String formattedToday = DateUtils.format(date)
        then:
            formattedToday != null
            formattedToday == expectedFormattedDay
    }
}
