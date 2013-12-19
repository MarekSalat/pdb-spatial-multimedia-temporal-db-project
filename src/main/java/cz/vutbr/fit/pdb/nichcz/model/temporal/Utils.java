package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Petr Přikryl
 * Date: 6.10.13
 * Time: 17:01
 *
 * Trida pro zakladni manipulaci s java Date objektem a pro jeho konvertovani do databazoveho formatu a zpet.
 */
public class Utils {

    public Date infinity;

    public Utils() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            infinity = formatter.parse("1.1.9999");
        } catch (ParseException ex) {ex.printStackTrace();  throw new RuntimeException(ex); }
    }

    public Date getInfinity() {
        return infinity;
    }

    public Date getActualDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(sdf.format(new Date()));
        } catch (ParseException ex) {ex.printStackTrace();  throw new RuntimeException(ex); }
    }

    public Long dateToDays(Date date) {
        return date.getTime() / (1000 * 60 * 60 * 24) + 1;
    }

    public Date daysToDate(Long days) {
        return new Date(days * 24 * 60 * 60 * 1000 - 1);
    }


    public enum MATCH_TYPE {
        //  ------     vts, vte - in DB      => e1
        //     ======  uts, ute - new period => e2
        LEFT_OVERLAP,
        //         ------
        //     ======
        RIGHT_OVERLAP,
        //       --
        //     ======
        INSIDE,
        // ---
        //     ======
        BEFORE,
        //            ---
        //     ======
        AFTER,
        //  ------------
        //     ======
        CONTAINS
    }

    public MATCH_TYPE findMatch(Date vts_, Date vte_, Date uts_, Date ute_) {
        /*
        vts = valid time start
        vte = valid time end
        uts = update time start
        ute = update time end
         */
        Long vts = dateToDays(vts_);
        Long vte = dateToDays(vte_);
        Long uts = dateToDays(uts_);
        Long ute = dateToDays(ute_);

        if (vts >= uts && vte <= ute) {
            return MATCH_TYPE.INSIDE;
        }
        else if (vts < uts && vte > ute) {
            return MATCH_TYPE.CONTAINS;
        }
        else if (vte <= uts) {
            return MATCH_TYPE.BEFORE;
        }
        else if (vts >= ute) {
            return MATCH_TYPE.AFTER;
        }
        else if (vts < uts && vte <= ute) {
            return MATCH_TYPE.LEFT_OVERLAP;
        }
        else if (vts >= uts && vte > ute) {
            return MATCH_TYPE.RIGHT_OVERLAP;
        }
        return null;
    }

}
