package cz.vutbr.fit.pdb.nichcz.model;

/**
 * User: Marek Salát
 * Date: 2.12.13
 * Time: 23:19
 *
 * Rozhrani pro entitu databaze.
 *
 * @param <PK>    Primarni klic entity.
 */
public interface Entity<PK> {
    /**
     * Ziska ID entity.
     * @return Vraci ID entity.
     */
    public PK getId();

    /**
     * Ziska jmeno tabulky, ve ktere je entita.
     * @return Vraci jmeno tabulky.
     */
    public String getTable();
}
