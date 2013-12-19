package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.model.Entity;

import java.util.Date;

/**
 * User: Petr Přikryl
 * Date: 15.12.13
 * Time: 0:48
 *
 * Rozhrani pro temporalni entitu databaze.
 */
public interface TemporalEntity<ID, VALID_FROM, VALID_TO> extends Entity {
    public Date getValidFrom();
    public Date getValidTo();
    public void setValidFrom(Date validFrom);
    public void setValidTo(Date validTo);
    public TemporalEntity<ID, VALID_FROM, VALID_TO> clone(Utils utils);
}
