package cz.vutbr.fit.pdb.nichcz.model;

/**
 * User: Marek Salát
 * Date: 2.12.13
 * Time: 23:19
 */
public interface Entity<PK> {
    public PK getId();
    public String getTable();
}
