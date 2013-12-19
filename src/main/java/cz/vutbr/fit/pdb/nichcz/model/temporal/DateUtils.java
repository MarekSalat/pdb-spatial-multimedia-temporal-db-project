package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 13.12.13
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */

public class DateUtils {
    public static Date trim(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime( date );
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
