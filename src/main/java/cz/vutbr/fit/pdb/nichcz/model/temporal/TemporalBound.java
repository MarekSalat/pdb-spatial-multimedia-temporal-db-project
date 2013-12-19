package cz.vutbr.fit.pdb.nichcz.model.temporal;

/**
 * User: Petr Přikryl
 * Date: 17.12.13
 * Time: 23:30
 *
 * Trida pro definovani temporalnich vazeb mezi tabulky.
 */
public class TemporalBound {
    public enum TYPE {
        FOREIGN_KEY,
        PRIMARY_KEY
    }

    public TemporalDBMapper mapper;
    public TYPE type;
    public String column;
    public String foreignColumn;
}
