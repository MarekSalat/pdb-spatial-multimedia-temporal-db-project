package cz.vutbr.fit.pdb.nichcz.model.temporal;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 17.12.13
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
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
